package thecommerce.jh.member.service;

import thecommerce.jh.member.common.enums.SortBy;
import thecommerce.jh.member.model.Member;

import java.util.List;

public interface MemberService {
    Member createMember(Member member);

    List<Member> retrieveMembers(int page, int pageSize, SortBy sortBy, boolean desc);

    Member updateMember(Member member);
}
