package thecommerce.jh.user.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import thecommerce.jh.user.common.enums.SortBy;
import thecommerce.jh.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class JpaUserRepository implements UserRepository {

    private final EntityManager entityManager;

    public JpaUserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User insert(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> findByUserId(String userId){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.where(entityManager.getCriteriaBuilder().equal(root.get("userId"), userId));

        return Optional.ofNullable(entityManager.createQuery(criteriaQuery).getSingleResult());
    }

    @Override
    public List<User> findByArguments(User user) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        List<Predicate> predicates = new ArrayList<>();

        addPredicateIfNotNull(predicates, user.getUserId(), root.get("userId"));
        addPredicateIfNotNull(predicates, user.getNickname(), root.get("nickname"));
        addPredicateIfNotNull(predicates, user.getPhoneNumber(), root.get("phoneNumber"));
        addPredicateIfNotNull(predicates, user.getEmail(), root.get("email"));

        if (!predicates.isEmpty()) {
            criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));
        }

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<User> findAll(int offset, int limit, SortBy sortBy, boolean desc) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);

        if (sortBy == SortBy.CREATED_AT) {
            criteriaQuery.orderBy(desc ? criteriaBuilder.desc(root.get("createdAt")) : criteriaBuilder.asc(root.get("createdAt")));
        } else {
            criteriaQuery.orderBy(desc ? criteriaBuilder.desc(root.get("name")) : criteriaBuilder.asc(root.get("name")));
        }

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public User update(User user) {
        return entityManager.merge(user);
    }

    private void addPredicateIfNotNull(List<Predicate> predicates, String value, Path<String> path) {
        if (value != null && !value.isEmpty()) {
            predicates.add(entityManager.getCriteriaBuilder().equal(path, value));
        }
    }
}