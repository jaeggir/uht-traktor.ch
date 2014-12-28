package ch.uhttraktor.website.rest.service;

import ch.uhttraktor.website.domain.PersistentToken;
import ch.uhttraktor.website.domain.User;
import ch.uhttraktor.website.persistence.PersistentTokenRepository;
import ch.uhttraktor.website.persistence.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

@Service
public class AuthService {

    private final Logger log = LogManager.getLogger(AuthService.class);

    private static final int DEFAULT_SERIES_LENGTH = 16;

    private static final int DEFAULT_TOKEN_LENGTH = 16;

    private SecureRandom random = new SecureRandom();

    @Value("${security.cookie.lifetime}")
    private int TOKEN_VALIDITY_DAYS;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private UserRepository userRepository;

    @Transactional
    public PersistentToken getAndUpdateToken(String[] cookieTokens, HttpServletRequest request) {

        PersistentToken token = getPersistentToken(cookieTokens);
        String login = token.getUser().getLogin();

        // Token also matches, so login is valid. Update the token value, keeping the *same* series number.
        log.debug("Refreshing persistent login token for user '{}', series '{}'", login, token.getSeries());
        token.setTokenDate(Instant.now());
        token.setTokenValue(generateTokenData());
        token.setIpAddress(request.getRemoteAddr());
        token.setUserAgent(request.getHeader("User-Agent"));

        // Set last login date
        User user = token.getUser();
        user.setLastLoginDate(Instant.now());
        userRepository.save(user);

        try {
            persistentTokenRepository.save(token);
        } catch (DataAccessException e) {
            log.error("Failed to update token", e);
            throw new RememberMeAuthenticationException("Autologin failed due to data access problem" + e);
        }
        return token;
    }

    @Transactional
    public PersistentToken onLoginSuccess(HttpServletRequest request, Authentication successfulAuthentication) {

        String login = successfulAuthentication.getName();

        log.debug("Creating new persistent login for user '{}'", login);
        User user = userRepository.findByLogin(login);

        // Set last login date
        user.setLastLoginDate(Instant.now());
        userRepository.save(user);

        PersistentToken token = new PersistentToken();
        token.setSeries(generateSeriesData());
        token.setUser(user);
        token.setTokenValue(generateTokenData());
        token.setTokenDate(Instant.now());
        token.setIpAddress(request.getRemoteAddr());
        token.setUserAgent(request.getHeader("User-Agent"));
        try {
            persistentTokenRepository.save(token);
        } catch (DataAccessException e) {
            log.error("Failed to save persistent token", e);
        }
        return token;
    }

    /**
     * When logout occurs, only invalidate the current token, and not all user sessions.
     * <p/>
     * The standard Spring Security implementations are too basic: they invalidate all tokens for the
     * current user, so when he logs out from one browser, all his other sessions are destroyed.
     */
    @Transactional
    public void logout(String[] cookieTokens) {
        try {
            PersistentToken token = getPersistentToken(cookieTokens);
            persistentTokenRepository.remove(token);
        } catch (InvalidCookieException ice) {
            log.info("Invalid cookie, no persistent token could be deleted");
        } catch (RememberMeAuthenticationException rmae) {
            log.debug("No persistent token found, so no token could be deleted");
        }
    }

    /**
     * Validate the token and return it.
     */
    private PersistentToken getPersistentToken(String[] cookieTokens) {
        if (cookieTokens.length != 2) {
            throw new InvalidCookieException("Cookie token did not contain " + 2 +
                    " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
        }

        final String presentedSeries = cookieTokens[0];
        final String presentedToken = cookieTokens[1];

        PersistentToken token = persistentTokenRepository.findBySeries(presentedSeries);

        if (token == null) {
            // No series match, so we can't authenticate using this cookie
            throw new RememberMeAuthenticationException("No persistent token found for series id: " + presentedSeries);
        }

        // We have a match for this user/series combination
        log.info("presentedToken={} / tokenValue={}", presentedToken, token.getTokenValue());
        if (!presentedToken.equals(token.getTokenValue())) {
            // Token doesn't match series value. Delete this session and throw an exception.
            persistentTokenRepository.deleteBySeries(token.getSeries());
            throw new RememberMeAuthenticationException("Invalid remember-me token (Series/token) mismatch. " +
                    "Implies previous cookie theft attack.");
        }

        if (token.getTokenDate().plus(TOKEN_VALIDITY_DAYS, ChronoUnit.DAYS).isBefore(Instant.now())) {
            persistentTokenRepository.remove(token);
            throw new RememberMeAuthenticationException("Remember-me login has expired");
        }
        return token;
    }

    private String generateSeriesData() {
        byte[] newSeries = new byte[DEFAULT_SERIES_LENGTH];
        random.nextBytes(newSeries);
        return new String(Base64.encode(newSeries));
    }

    private String generateTokenData() {
        byte[] newToken = new byte[DEFAULT_TOKEN_LENGTH];
        random.nextBytes(newToken);
        return new String(Base64.encode(newToken));
    }

}
