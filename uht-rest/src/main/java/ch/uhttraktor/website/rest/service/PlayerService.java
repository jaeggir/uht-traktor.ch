package ch.uhttraktor.website.rest.service;

import ch.uhttraktor.website.domain.Player;
import ch.uhttraktor.website.persistence.PlayerRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class PlayerService extends BaseService<Player> {

    @Inject
    private PlayerRepository playerRepository;

    protected PlayerRepository getRepository() {
        return playerRepository;
    }
}
