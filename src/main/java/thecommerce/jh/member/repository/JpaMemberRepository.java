package thecommerce.jh.member.repository;

import org.springframework.stereotype.Repository;
import thecommerce.jh.member.model.Member;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaMemberRepository implements MemberRepository {

    private final EntityManager entityManager;

    public JpaMemberRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Member insert(Member member) {
        entityManager.persist(member);

        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Member.class, id));
    }

    public List<Member> findByArguments(Member member) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        List<Predicate> predicates = new ArrayList<>();

        if (member.getUserId() != null && !member.getUserId().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("userId"), member.getUserId()));
        }

        if (member.getNickname() != null && !member.getNickname().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("nickname"), member.getNickname()));
        }

        if (member.getPhoneNumber() != null && !member.getPhoneNumber().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("phoneNumber"), member.getPhoneNumber()));
        }

        if (member.getEmail() != null && !member.getEmail().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("email"), member.getEmail()));
        }

        if (!predicates.isEmpty()) {
            criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));
        }

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Member> findAll(int offset, int limit) {

        return entityManager.createQuery("SELECT m FROM Member m", Member.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public Member update(Member member) {
        return entityManager.merge(member);
    }
}