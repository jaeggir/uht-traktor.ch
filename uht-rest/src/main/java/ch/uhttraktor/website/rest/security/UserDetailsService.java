package ch.uhttraktor.website.rest.security;

import ch.uhttraktor.website.domain.Authority;
import ch.uhttraktor.website.domain.User;
import ch.uhttraktor.website.persistence.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;

@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final Logger log = LogManager.getLogger(UserDetailsService.class);

    @Inject
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating '{}'", login);
        String lowercaseLogin = login.toLowerCase();
        User userFromDatabase = userRepository.findByLogin(lowercaseLogin);
        if (userFromDatabase == null) {
            throw new UsernameNotFoundException("User '" + lowercaseLogin + "' was not found in the database");
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (Authority authority : userFromDatabase.getAuthorities()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());
            authorities.add(grantedAuthority);
        }

        String password = userFromDatabase.getPassword();
        return new org.springframework.security.core.userdetails.User(lowercaseLogin, password, authorities);
    }
}
