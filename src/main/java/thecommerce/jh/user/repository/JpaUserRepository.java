package thecommerce.jh.user.repository;

import org.springframework.stereotype.Repository;
import thecommerce.jh.user.common.enums.SortBy;
import thecommerce.jh.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
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

    public List<User> findByArguments(User user) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        List<Predicate> predicates = new ArrayList<>();

        if (user.getUserId() != null && !user.getUserId().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("userId"), user.getUserId()));
        }

        if (user.getNickname() != null && !user.getNickname().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("nickname"), user.getNickname()));
        }

        if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("phoneNumber"), user.getPhoneNumber()));
        }

        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("email"), user.getEmail()));
        }

        if (!predicates.isEmpty()) {
            criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));
        }

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<User> findAll(int offset, int limit, SortBy sortBy, boolean desc) {
        String queryString = "SELECT m FROM User m ORDER BY ";

        if (sortBy == SortBy.CREATED_AT) {
            queryString += "m.createdAt";
        } else {
            queryString += "m.name";
        }

        if (desc) {
            queryString += " DESC";
        }

        return entityManager.createQuery(queryString, User.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }


    @Override
    public User update(User user) {
        return entityManager.merge(user);
    }
}