package ch.uhttraktor.website.persistence;

import ch.uhttraktor.website.domain.PersistentToken;
import ch.uhttraktor.website.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Repository
public class PersistentTokenRepository extends BaseRepository<PersistentToken> {

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public List<PersistentToken> findByTokenDateBefore(Instant tokenDate) {
        return findAllByProperty("tokenDate", tokenDate);
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public PersistentToken findBySeries(String series) {
        return findByProperty("series", series);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteBySeries(String series) {
        PersistentToken entity = findBySeries(series);
        if (entity != null) {
            remove(entity);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void removeByUser(User user) {
        List<PersistentToken> tokens = findAllByProperty("user", user);
        for (PersistentToken token : tokens) {
            remove(token);
        }
    }
}