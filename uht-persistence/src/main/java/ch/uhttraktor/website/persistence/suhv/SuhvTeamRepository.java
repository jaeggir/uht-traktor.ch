package ch.uhttraktor.website.persistence.suhv;

import ch.uhttraktor.website.domain.suhv.SuhvTeam;
import ch.uhttraktor.website.persistence.BaseRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Repository
public class SuhvTeamRepository extends BaseRepository<SuhvTeam> {

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public SuhvTeam findByIdAndSeason(Integer id, Integer season) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("id", id);
        properties.put("season", season);
        return findByProperties(properties);
    }
}