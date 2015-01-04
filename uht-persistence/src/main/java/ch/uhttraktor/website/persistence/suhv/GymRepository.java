package ch.uhttraktor.website.persistence.suhv;

import ch.uhttraktor.website.domain.suhv.Gym;
import ch.uhttraktor.website.persistence.BaseRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class GymRepository extends BaseRepository<Gym> {

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public Gym findById(Integer id) {
        return findByProperty("id", id);
    }
}