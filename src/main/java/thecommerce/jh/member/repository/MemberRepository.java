package thecommerce.jh.member.repository;

import thecommerce.jh.member.model.Member;

import java.util.List;

public interface MemberRepository {
    Member insert(Member member);

    List<Member> findAll();

    Member update(Member member);
}
