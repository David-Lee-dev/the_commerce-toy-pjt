package thecommerce.jh.member.service;

import thecommerce.jh.member.SortBy;
import thecommerce.jh.member.model.Member;

public interface MemberService {
    Member createMember(Member member);

    Member retrieveMembers(int page, int pageSize, SortBy sortBy);

    Member updateMember(Member member);
}
