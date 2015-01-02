package ch.uhttraktor.website.rest.service;

import ch.uhttraktor.website.domain.User;
import ch.uhttraktor.website.persistence.BaseRepository;
import ch.uhttraktor.website.persistence.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
public class UserService extends BaseService<User> {

    @Inject
    private UserRepository userRepository;

    @Override
    protected BaseRepository<User> getRepository() {
        return userRepository;
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("No authentication - should not happen as all requests must be authenticated");
        } else {
            UserDetails currentUser = (UserDetails) authentication.getPrincipal();
            return userRepository.findByLogin(currentUser.getUsername());
        }
    }

}
