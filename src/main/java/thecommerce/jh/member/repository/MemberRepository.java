package thecommerce.jh.member.repository;

import thecommerce.jh.member.model.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member insert(Member member);

    Optional<Member> findById(Long id);

    List<Member> findByArguments(Member member);

    List<Member> findAll(int offset, int limit);

    Member update(Member member);
}
