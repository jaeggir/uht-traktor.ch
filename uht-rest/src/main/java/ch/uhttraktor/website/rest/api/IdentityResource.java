package ch.uhttraktor.website.rest.api;

import ch.uhttraktor.website.domain.User;
import ch.uhttraktor.website.rest.service.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

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
    public User getCurrentUser(HttpServletRequest request) {
        String remoteUser = request.getRemoteUser();
        if (remoteUser != null) {
            User user = userService.getCurrentUser();
            return user;
        } else {
            return null;
        }
    }

    /**
     * POST  /identity/login -> login endpoint
     */
    @RequestMapping(value = "/identity/login", method = RequestMethod.POST, produces = "application/json")
    public User login(@RequestBody(required = true) UserDto userDto) {
        System.out.println("username=" + userDto.getUsername() + ", pw=" + userDto.getPassword());
        return null;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    static class UserDto {
        private String username;
        private String password;
    }

}
