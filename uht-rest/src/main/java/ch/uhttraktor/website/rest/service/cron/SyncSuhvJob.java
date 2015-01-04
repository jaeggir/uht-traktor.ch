package ch.uhttraktor.website.rest.service.cron;

import ch.uhttraktor.website.domain.suhv.*;
import ch.uhttraktor.website.persistence.suhv.ClubRepository;
import ch.uhttraktor.website.persistence.suhv.GymRepository;
import ch.uhttraktor.website.persistence.suhv.SuhvTeamRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class SyncSuhvJob extends TransactionalTask {

    private final Logger log = LogManager.getLogger(SyncSuhvJob.class);

    private static final String API_END_POINT = "http://api.swissunihockey.ch/rest/v1.0/";

    @Inject
    private SuhvTeamRepository suhvTeamRepository;

    @Inject
    private ClubRepository clubRepository;

    @Inject
    private GymRepository gymRepository;

    @Value("${suhv.api.key}")
    private String apiKey;

    private AtomicBoolean running = new AtomicBoolean(false);

    private Integer currentSeason;

    @PostConstruct
    public void init() {
        super.init();

        // set current season, not sure if there is a better way?
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.MONTH) < Calendar.JUNE) {
            currentSeason = calendar.get(Calendar.YEAR) - 1;
        } else {
            currentSeason = calendar.get(Calendar.YEAR);
        }
    }


    @Scheduled(fixedRate = Long.MAX_VALUE)
    public void afterStartup() {
        runJob();
    }

    @Scheduled(cron="${suhv.api.cron}")
    public void runJob() {

        long start = System.currentTimeMillis();
        log.info("Starting sync");

        if (running.compareAndSet(false, true)) {

            Client client = ClientBuilder.newClient(new ClientConfig());
            try {

                getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {

                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {

                        // sync all clubs
                        List<Club> clubs = clubRepository.findAll();
                        for (Club club : clubs) {

                            log.info("Syncing club '{}'", club.getName());

                            // get possible seasons for this club based on starting season and current date
                            List<Integer> seasons = getSeasons(club);
                            for (Integer season : seasons) {

                                // reload club as we flush and clear later
                                club = clubRepository.findOne(club.getUuid());

                                // get teams for the given club and season
                                Set<SuhvTeam> teams = getTeams(client, club, season);
                                for (SuhvTeam team : teams) {

                                    log.info("Syncing season {} of '{}' ({})", season, team.getTeamName(), team.getId());

                                    // finally, sync the team
                                    syncTeam(client, club, team, season);
                                }

                                // for performance reasons, flush & clear here
                                clubRepository.flush();
                                clubRepository.clear();
                            }
                        }
                    }
                });

            } finally {
                client.close();
                running.set(false);
                log.info("Sync done, total time={}ms", (System.currentTimeMillis() - start));
            }
        } else {
            log.info("Sync aborted - already running");
        }
    }

    private List<Integer> getSeasons(Club club) {
        Integer start = club.getFirstSuhvSeason();
        Integer end = currentSeason;
        if (start > end) {
            return Collections.emptyList();
        } else {
            List<Integer> result = new LinkedList<>();
            while (start <= end) {
                result.add(start++);
            }
            return result;
        }
    }

    private Set<SuhvTeam> getTeams(Client client, Club club, Integer season) {
        TeamResponse teams = client.target(API_END_POINT)
                .path("clubs/" + club.getId() + "/teams")
                .queryParam("season", season)
                .request(MediaType.APPLICATION_XML)
                .header("apikey", apiKey)
                .get(TeamResponse.class);

        // set attributes we don't get from the API
        for (SuhvTeam team : teams.getTeams()) {
            team.setClub(club);
            team.setSeason(season);
        }

        return teams.getTeams();
    }

    private void syncTeam(Client client, Club club, SuhvTeam team, Integer season) {
        requestStandings(client, team, season);
        requestGames(client, team, season);

        SuhvTeam existingTeam = suhvTeamRepository.findByIdAndSeason(team.getId(), season);
        if (existingTeam == null) {
            club.getTeams().add(team);
            clubRepository.save(club);
        } else {
            // TODO
        }
    }

    private void requestStandings(Client client, SuhvTeam team, Integer season) {

        Standings standings = client.target(API_END_POINT)
                .path("teams/" + team.getId() + "/table")
                .queryParam("season", season)
                .request(MediaType.APPLICATION_XML)
                .header("apikey", apiKey)
                .get(Standings.class);

        standings.setTeam(team);
        standings.setSeason(season);
        for (StandingsEntry entry :standings.getEntries()) {
            entry.setStandings(standings);
        }

        team.setStandings(standings);
    }

    private void requestGames(Client client, SuhvTeam team, Integer season) {

        GamesResponse games = client.target(API_END_POINT)
                .path("teams/" + team.getId() + "/games")
                .queryParam("season", season)
                .queryParam("limit", 100)
                .queryParam("order", "ASC")
                .request(MediaType.APPLICATION_XML)
                .header("apikey", apiKey)
                .get(GamesResponse.class);

        for (Game game : games.getGames()) {
            game.updateDate();
            game.setTeam(team);
            updateGym(client, game);
        }

        team.setGames(games.getGames());
    }

    private void updateGym(Client client, Game game) {
        if (game.getGym() == null || game.getGym().getId() == null || game.getGym().getId() == -1) {
            // no gym found in response - should not happen, but you never know
            game.setGym(null);
            return;
        } else {
            try {
                Gym gym = client.target(API_END_POINT)
                        .path("gyms/" + game.getGym().getId())
                        .request(MediaType.APPLICATION_XML)
                        .header("apikey", apiKey)
                        .get(Gym.class);

                Gym existingGym = gymRepository.findById(game.getGym().getId());
                if (existingGym == null) {
                    // new gym
                    existingGym = gymRepository.save(gym);
                } else {

                    // update existing gym
                    existingGym.setName(trim(gym.getName()));
                    existingGym.setStreet(trim(gym.getStreet()));
                    existingGym.setZip(gym.getZip());
                    existingGym.setCity(trim(gym.getCity()));
                    existingGym.setCountry(trim(gym.getCountry()));
                    existingGym.setPosition(gym.getPosition());

                    // merge
                    existingGym = gymRepository.save(existingGym);

                }
                // assign updated gym to game
                game.setGym(existingGym);
            } catch (javax.ws.rs.NotFoundException e) {
                log.debug("Gym with id='{}' not found. msg={}", game.getGym().getId(), e.getMessage());
                game.setGym(null);
            }
        }
    }

    private String trim(String str) {
        if (str == null || str.length() == 0) {
            return null;
        } else {
            str = str.trim();
            if (str.length() == 0) {
                return null;
            } else {
                return "''".equals(str) ? null : str;
            }
        }
    }

}
