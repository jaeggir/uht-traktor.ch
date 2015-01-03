package ch.uhttraktor.website.rest.api;

import ch.uhttraktor.website.domain.Player;
import ch.uhttraktor.website.rest.service.PlayerService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static ch.uhttraktor.website.domain.SecurityRole.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Secured(value = {USER, TEAMS})
public class PlayerResource {

    @Inject
    private PlayerService playerService;

    /**
     * GET  /players/{uuid} -> get player specified by uuid.
     */
    @Secured(value = ANONYMOUS)
    @RequestMapping(value = "/players/{uuid}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public Player getPlayer(HttpServletResponse response, @PathVariable(value = "uuid") String uuid) {
        return playerService.findOne(response, uuid);
    }

    /**
     * GET  /players -> get all players
     */
    @Secured(value = ANONYMOUS)
    @RequestMapping(value = "/players", method = RequestMethod.GET, produces = "application/json")
    public List<Player> getPlayers() {
        return playerService.findAll();
    }

    /**
     * DELETE  /players/{uuid} -> delete player specified by uuid.
     */
    @RequestMapping(value = "/players/{uuid}", method = RequestMethod.DELETE)
    public void deletePlayer(HttpServletResponse response, @PathVariable(value = "uuid") String uuid) {
        playerService.delete(response, uuid);
    }

    /**
     * POST  /players -> create or update player
     */
    @RequestMapping(value = "/players", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public Player createOrUpdatePlayer(HttpServletResponse response, @RequestBody Player player) {
        return playerService.createOrUpdate(response, player);
    }

}
