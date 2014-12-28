package ch.uhttraktor.website.rest.service;

import ch.uhttraktor.website.domain.User;
import ch.uhttraktor.website.persistence.UserRepository;
import ch.uhttraktor.website.rest.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
public class UserService {

    @Inject
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        return userRepository.findByLogin(SecurityUtils.getCurrentLogin());
    }

}
