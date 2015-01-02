package ch.uhttraktor.website.rest.api;

import ch.uhttraktor.website.domain.User;
import ch.uhttraktor.website.rest.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import static ch.uhttraktor.website.domain.SecurityRole.ANONYMOUS;

@RestController
@Secured(value = {ANONYMOUS})
public class IdentityResource {

    @Inject
    private UserService userService;

    /**
     * GET  /identity/user -> get currently logged in user or null.
     */
    @RequestMapping(value = "/identity/user", method = RequestMethod.GET, produces = "application/json")
    public User getCurrentUser() {
        return userService.getCurrentUser();
    }

}
