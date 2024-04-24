package thecommerce.jh.member.service;

import org.springframework.stereotype.Service;
import thecommerce.jh.member.common.enums.ErrorCode;
import thecommerce.jh.member.common.enums.SortBy;
import thecommerce.jh.member.common.exception.CustomException;
import thecommerce.jh.member.model.Member;
import thecommerce.jh.member.repository.MemberRepository;

import java.util.List;
import java.util.Objects;

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
            checkDuplication(member, foundMembers);
        }

        return memberRepository.insert(member);
    }

    @Override
    public List<Member> retrieveMembers(int page, int pageSize, SortBy sortBy, boolean desc) {
        int offset = pageSize * (page - 1);

        return memberRepository.findAll(offset, pageSize, sortBy, desc);
    }

    @Override
    public Member updateMember(Member member) {
        if(!memberRepository.findById(member.getId()).isPresent()) {
            throw new CustomException(ErrorCode.NON_EXISTENT);
        }

        return memberRepository.update(member);
    }

    private static void checkDuplication(Member member, List<Member> compareList) {
        for (Member compare : compareList) {
            if (Objects.equals(compare.getUserId(), member.getUserId())) {
                throw new CustomException(ErrorCode.DUPLICATED_USER_ID);
            }

            if (Objects.equals(compare.getPhoneNumber(), member.getPhoneNumber())) {
                throw new CustomException(ErrorCode.DUPLICATED_PHONE_NUMBER);
            }

            if (Objects.equals(compare.getEmail(), member.getEmail())) {
                throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
            }

            if (Objects.equals(compare.getNickname(), member.getNickname())) {
                throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
            }
        }
    }
}
