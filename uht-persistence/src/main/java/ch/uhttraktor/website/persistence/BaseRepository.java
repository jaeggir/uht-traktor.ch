package ch.uhttraktor.website.persistence;

import ch.uhttraktor.website.domain.BaseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class BaseRepository<Entity extends BaseEntity> {

    @PersistenceContext
    protected EntityManager em;

    protected Class<Entity> clazz;

    @SuppressWarnings("unchecked")
    public BaseRepository() {
        Class<?> klass = getClass();
        while (!(klass.getGenericSuperclass() instanceof ParameterizedType)) {
            klass = klass.getSuperclass();
            if (klass.equals(Object.class)) {
                throw new IllegalStateException();
            }
        }
        ParameterizedType genericSuperclass = (ParameterizedType) klass.getGenericSuperclass();
        this.clazz = (Class<Entity>) genericSuperclass.getActualTypeArguments()[0];
    }

    // INSERT / UPDATE

    @Transactional(propagation = Propagation.MANDATORY)
    public void save(Collection<Entity> entities) {
        entities.forEach(entity -> save(entity));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void save(Entity... entities) {
        for (Entity entity : entities) {
            save(entity);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Entity save(Entity entity) {
        if (entity.getUuid() == null) {
            em.persist(entity);
            return entity;
        } else {
            return em.merge(entity);
        }
    }

    // DELETE

    @Transactional(propagation = Propagation.MANDATORY)
    public void remove(Entity entity) {
        em.remove(entity);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void remove(UUID uuid) {
        remove(findOne(uuid));
    }

    // SELECT

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public Entity findOne(UUID uuid) {
        return em.find(clazz, uuid);
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public List<Entity> findByUuids(List<UUID> uuids) {
        if (uuids == null || uuids.isEmpty()) {
            return Collections.emptyList();
        } else {
            TypedQuery<Entity> query = em.createQuery("from " + clazz.getSimpleName() +
                    " c WHERE c.uuid IN :uuids", clazz).setParameter("uuids", uuids);

            return query.getResultList();
        }
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public List<Entity> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Entity> cq = cb.createQuery(clazz);
        Root<Entity> rootEntry = cq.from(clazz);
        CriteriaQuery<Entity> all = cq.select(rootEntry);
        return em.createQuery(all).getResultList();
    }

    protected Entity findByProperty(String property, Object value) {
        TypedQuery<Entity> query = em.createQuery(createQueryByProperty(property, value));
        return findOne(query.getResultList());
    }

    protected List<Entity> findAllByProperty(String property, Object value) {
        return em.createQuery(createQueryByProperty(property, value)).getResultList();
    }

    protected Entity findByProperties(Map<String, Object> properties) {
        TypedQuery<Entity> query = em.createQuery(createQueryByProperties(properties));
        return findOne(query.getResultList());
    }

    protected List<Entity> findAllByProperties(Map<String, Object> properties) {
        return em.createQuery(createQueryByProperties(properties)).getResultList();
    }

    private CriteriaQuery<Entity> createQueryByProperty(String property, Object value) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Entity> cq = cb.createQuery(clazz);
        Root<Entity> root = cq.from(clazz);
        cq = cq.where(cb.equal(root.get(property), value));
        return cq;
    }

    private CriteriaQuery<Entity> createQueryByProperties(Map<String, Object> properties) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Entity> cq = cb.createQuery(clazz);
        Root<Entity> root = cq.from(clazz);
        List<Predicate> predicates = new ArrayList<Predicate>();
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            predicates.add(cb.equal(root.get(entry.getKey()), entry.getValue()));
        }
        cq.where(predicates.toArray(new Predicate[]{}));
        return cq;
    }

    protected Entity findOne(List<Entity> result) {
        if (result == null || result.isEmpty()) {
            return null;
        } else if (result.size() == 1) {
            return result.get(0);
        } else {
            throw new IllegalStateException("Result count was " + result.size() + ", but 1 or 0 expected.");
        }
    }

}