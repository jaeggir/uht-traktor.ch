package ch.uhttraktor.website.rest.service;

import ch.uhttraktor.website.domain.Team;
import ch.uhttraktor.website.persistence.TeamRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class TeamService extends BaseService<Team> {

    @Inject
    private TeamRepository teamRepository;

    protected TeamRepository getRepository() {
        return teamRepository;
    }
}
