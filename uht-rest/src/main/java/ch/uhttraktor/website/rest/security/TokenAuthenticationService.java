package ch.uhttraktor.website.rest.security;

import ch.uhttraktor.website.domain.SecurityRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static ch.uhttraktor.website.domain.SecurityRole.ANONYMOUS;

/**
 * Service responsible for everything around authentication, token checks, anonymous user handling etc. This class
 * does not care about HTTP protocol at all.
 */
public class TokenAuthenticationService {

    protected final Logger log = LogManager.getLogger(TokenAuthenticationService.class);

    private static final String ANONYMOUS_TOKEN = "qfRRrkEVwGWaq1TyZ4IxMJ9x69Lx/xu5lk2ji2d5iUA=";

    private static final UserDetails ANONYMOUS_USER_DETAILS = new UserDetails() {

        private final List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(ANONYMOUS);

        @Override
        public List<GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public String getUsername() {
            return SecurityRole.ANONYMOUS_USER_NAME;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    };

    private final AuthenticationManager authenticationManager;
    private final InMemoryTokenManager tokenManager;

    public TokenAuthenticationService(AuthenticationManager authenticationManager, InMemoryTokenManager tokenManager) {
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
    }

    public TokenInfo authenticate(String username, String password) {
        if (SecurityRole.ANONYMOUS_USER_NAME.equals(username)) {
            log.debug("Anonymous login");
            return new TokenInfo(ANONYMOUS_TOKEN, ANONYMOUS_USER_DETAILS);
        } else {
            log.debug("Login - username='{}'", username);

            // Here principal=username, credentials=password
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
            try {
                authentication = authenticationManager.authenticate(authentication);
                // Here principal=UserDetails, credentials=null (security reasons)
                SecurityContextHolder.getContext().setAuthentication(authentication);

                if (authentication.getPrincipal() != null) {
                    UserDetails userContext = (UserDetails) authentication.getPrincipal();
                    return tokenManager.createNewToken(userContext);
                }
            } catch (AuthenticationException e) {
                log.error("Login failed", e);
            }

            return null;
        }
    }

    public boolean checkToken(String token) {
        if (ANONYMOUS_TOKEN.equals(token)) {

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    ANONYMOUS_USER_DETAILS, null, ANONYMOUS_USER_DETAILS.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        } else {
            UserDetails userDetails = tokenManager.getUserDetails(token);
            if (userDetails == null) {
                return false;
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        }
    }

    public void logout(String token) {
        if (!ANONYMOUS_TOKEN.equals(token)) {
            String username = tokenManager.removeToken(token);
            log.debug("Logout user: '{}'", username);
        }
        SecurityContextHolder.clearContext();
    }

}
