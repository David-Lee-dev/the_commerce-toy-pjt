package thecommerce.jh.member.service;

import org.springframework.stereotype.Service;
import thecommerce.jh.member.enums.SortBy;
import thecommerce.jh.member.model.Member;
import thecommerce.jh.member.repository.MemberRepository;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member createMember(Member member) {
        List<Member> foundMembers = memberRepository.findByArguments(member);

        if(!foundMembers.isEmpty()) {
            throw new RuntimeException("user input violation");
        }

        return memberRepository.insert(member);
    }

    @Override
    public Member retrieveMembers(int page, int pageSize, SortBy sortBy) {
        return null;
    }

    @Override
    public Member updateMember(Member member) {
        return null;
    }
}