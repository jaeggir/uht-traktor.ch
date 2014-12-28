package ch.uhttraktor.website.rest.security;

import ch.uhttraktor.website.domain.PersistentToken;
import ch.uhttraktor.website.rest.service.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Custom implementation of Spring Security's RememberMeServices.
 * <p/>
 * Persistent tokens are used by Spring Security to automatically log in users.
 * <p/>
 * This is a specific implementation of Spring Security's remember-me authentication, but it is much
 * more powerful than the standard implementations:
 * <ul>
 * <li>It allows a user to see the list of his currently opened sessions, and invalidate them</li>
 * <li>It stores more information, such as the IP address and the user agent, for audit purposes</li>
 * <li>When a user logs out, only his current session is invalidated, and not all of his sessions</li>
 * </ul>
 * <p/>
 * This is inspired by:
 * <ul>
 * <li><a href="http://jaspan.com/improved_persistent_login_cookie_best_practice">Improved Persistent Login Cookie
 * Best Practice</a></li>
 * <li><a href="https://github.com/blog/1661-modeling-your-app-s-user-session">Github's "Modeling your App's User Session"</a></li></li>
 * </ul>
 * <p/>
 * The main algorithm comes from Spring Security's PersistentTokenBasedRememberMeServices, but this class
 * couldn't be cleanly extended.
 * <p/>
 */
public class CustomPersistentRememberMeServices extends AbstractRememberMeServices {

    private final Logger log = LogManager.getLogger(CustomPersistentRememberMeServices.class);

    private int tokenValidityDays;

    private AuthService authService;

    public CustomPersistentRememberMeServices(String rememberMeKey, int tokenValidityDays, AuthService authService,
            org.springframework.security.core.userdetails.UserDetailsService userDetailsService) {
        super(rememberMeKey, userDetailsService);
        this.tokenValidityDays = tokenValidityDays;
        this.authService = authService;
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request,
                                                 HttpServletResponse response) {

        PersistentToken token = authService.getAndUpdateToken(cookieTokens, request);
        addCookie(token, request, response);
        try {
            return getUserDetailsService().loadUserByUsername(token.getUser().getLogin());
        } catch (DataAccessException e) {
            log.error("Failed to update token", e);
            throw new RememberMeAuthenticationException("Autologin failed due to data access problem" + e);
        }
    }

    @Override
    protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
                                  Authentication successfulAuthentication) {

        PersistentToken token = authService.onLoginSuccess(request, successfulAuthentication);
        addCookie(token, request, response);
    }

    /**
     * When logout occurs, only invalidate the current token, and not all user sessions.
     * <p/>
     * The standard Spring Security implementations are too basic: they invalidate all tokens for the
     * current user, so when he logs out from one browser, all his other sessions are destroyed.
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String rememberMeCookie = extractRememberMeCookie(request);
        if (rememberMeCookie != null && rememberMeCookie.length() != 0) {
            try {
                String[] cookieTokens = decodeCookie(rememberMeCookie);
                authService.logout(cookieTokens);
            } catch (InvalidCookieException ice) {
                log.info("Invalid cookie, no persistent token could be deleted");
            }
        }
        super.logout(request, response, authentication);
    }

    private void addCookie(PersistentToken token, HttpServletRequest request, HttpServletResponse response) {
        setCookie(new String[]{token.getSeries(), token.getTokenValue()}, 60 * 60 * 24 * tokenValidityDays,
                request, response);
    }
}
