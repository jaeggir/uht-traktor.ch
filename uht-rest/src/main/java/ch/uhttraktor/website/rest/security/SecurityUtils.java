package ch.uhttraktor.website.rest.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public final class SecurityUtils {

    private SecurityUtils() {
        // utility class - no public constructor needed
    }

    public static String getCurrentLogin() {
        return getCurrentUser().getUsername();
    }

    private static UserDetails getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return (UserDetails) securityContext.getAuthentication().getPrincipal();
    }
}
