package ch.uhttraktor.website.rest.api;

import ch.uhttraktor.website.domain.Team;
import ch.uhttraktor.website.rest.service.TeamService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static ch.uhttraktor.website.domain.SecurityRole.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Secured(value = {USER, TEAMS})
public class TeamResource {

    @Inject
    private TeamService teamService;

    /**
     * GET  /teams/{uuid} -> get team specified by uuid.
     */
    @Secured(value = ANONYMOUS)
    @RequestMapping(value = "/teams/{uuid}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public Team getTeam(HttpServletResponse response, @PathVariable(value = "uuid") String uuid) {
        return teamService.findOne(response, uuid);
    }

    /**
     * GET  /teams -> get all teams
     */
    @Secured(value = ANONYMOUS)
    @RequestMapping(value = "/teams", method = RequestMethod.GET, produces = "application/json")
    public List<Team> getTeams() {
        return teamService.findAll();
    }

    /**
     * DELETE  /teams/{uuid} -> delete team specified by uuid.
     */
    @RequestMapping(value = "/teams/{uuid}", method = RequestMethod.DELETE)
    public void deleteTeam(HttpServletResponse response, @PathVariable(value = "uuid") String uuid) {
        teamService.delete(response, uuid);
    }

    /**
     * POST  /teams -> create or update team
     */
    @RequestMapping(value = "/teams", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public Team createOrUpdateTeam(HttpServletResponse response, @RequestBody Team team) {
        return teamService.createOrUpdate(response, team);
    }

}
