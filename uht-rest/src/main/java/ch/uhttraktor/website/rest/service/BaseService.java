package ch.uhttraktor.website.rest.service;

import ch.uhttraktor.website.domain.BaseEntity;
import ch.uhttraktor.website.persistence.BaseRepository;
import ch.uhttraktor.website.persistence.util.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


public abstract class BaseService<T extends BaseEntity> {

    protected final Logger log = LogManager.getLogger(BaseService.class);

    protected abstract BaseRepository<T> getRepository();

    @Transactional(readOnly = true)
    public T findOne(HttpServletResponse response, String uuidString) {
        UUID uuid = uuidFromString(uuidString);
        if (uuid == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            T entity = getRepository().findOne(uuid);
            if (entity == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            return entity;
        }
    }

    @Transactional(readOnly = true)
    public Page<T> find(Integer pageSize, Integer page) {
        return getRepository().find(pageSize, page);
    }

    @Transactional
    public void delete(HttpServletResponse response, String uuidString) {
        UUID uuid = uuidFromString(uuidString);
        if (uuid == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            T entity = getRepository().findOne(uuid);
            if (entity == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } else {
                getRepository().remove(entity);
            }
        }
    }

    @Transactional
    public T createOrUpdate(HttpServletResponse response, T entity) {
        if (entity.getUuid() != null) {
            // existing entity - check if it really exists and set an error status if not
            T existingEntity = getRepository().findOne(entity.getUuid());
            if (existingEntity == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return entity;
            }
        }

        // persist or merge entity
        return getRepository().save(entity);
    }

    protected UUID uuidFromString(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            log.debug("Illegal uuid: {}", uuid, e);
            return null;
        }
    }
}
