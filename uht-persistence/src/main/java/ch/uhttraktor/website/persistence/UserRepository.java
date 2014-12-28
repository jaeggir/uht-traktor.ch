package ch.uhttraktor.website.persistence;

import ch.uhttraktor.website.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserRepository extends BaseRepository<User> {

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public User findByLogin(String login) {
        return findByProperty("login", login);
    }

}