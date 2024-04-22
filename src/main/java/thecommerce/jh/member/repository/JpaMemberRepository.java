package thecommerce.jh.member.repository;

import org.springframework.stereotype.Repository;
import thecommerce.jh.member.model.Member;

import javax.persistence.EntityManager;
import java.util.List;

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
    public List<Member> findAll() {
        return entityManager.createQuery("SELECT m FROM Member m", Member.class).getResultList();
    }

    @Override
    public Member update(Member member) {
        return entityManager.merge(member);
    }
}