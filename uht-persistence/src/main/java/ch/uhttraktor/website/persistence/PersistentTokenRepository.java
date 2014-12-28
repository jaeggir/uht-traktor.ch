package ch.uhttraktor.website.persistence;

import ch.uhttraktor.website.domain.PersistentToken;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PersistentTokenRepository extends BaseRepository<PersistentToken> {

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

}