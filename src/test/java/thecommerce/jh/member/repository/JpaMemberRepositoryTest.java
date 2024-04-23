package thecommerce.jh.member.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import thecommerce.jh.member.model.Member;

import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class JpaMemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void insert() {
        Member member = Member.builder()
                .userId("test_userId")
                .password("test_password")
                .name("test_name")
                .build();
        Member newMember = memberRepository.insert(member);

        assertThat(newMember).isEqualTo(member);
    }

    @Test
    void findById() {
        Member member = Member.builder()
                .userId("test_userId")
                .password("test_password")
                .name("test_name")
                .build();

        Member newMember = memberRepository.insert(member);
        Member foundMember = memberRepository.findById(newMember.getId()).get();

        assertThat(newMember).isEqualTo(foundMember);
    }

    @Test
    void findByArguments() {
        Member member = Member.builder()
                .userId("test_userId")
                .password("test_password")
                .name("test_name")
                .nickname("test_nickname")
                .phoneNumber("010-0000-0000")
                .email("test@test.com")
                .build();

        memberRepository.insert(member);

        List<Member> foundByUserId = memberRepository.findByArguments(Member.builder().userId("test_userId").build());
        List<Member> foundByNickname = memberRepository.findByArguments(Member.builder().nickname("test_nickname").build());
        List<Member> foundByPhoneNumber = memberRepository.findByArguments(Member.builder().phoneNumber("010-0000-0000").build());
        List<Member> foundByEmail = memberRepository.findByArguments(Member.builder().email("test@test.com").build());
        List<Member> foundByMultiArguments = memberRepository.findByArguments(Member.builder().phoneNumber("010-0000-0000").email("test@test.com").build());

        assertThat(foundByUserId.size()).isEqualTo(1);
        assertThat(foundByNickname.size()).isEqualTo(1);
        assertThat(foundByPhoneNumber.size()).isEqualTo(1);
        assertThat(foundByEmail.size()).isEqualTo(1);
        assertThat(foundByMultiArguments.size()).isEqualTo(1);
    }

    @Test
    void findAll() {
        Member memberA = Member.builder()
                .userId("test_userId_A")
                .password("test_password_A")
                .name("test_name_A")
                .build();
        Member memberB = Member.builder()
                .userId("test_userId_B")
                .password("test_password_B")
                .name("test_name_B")
                .build();

        memberRepository.insert(memberA);
        memberRepository.insert(memberB);
        List<Member> members = memberRepository.findAll(0, 10);
        List<Member> limitedMembers = memberRepository.findAll(0, 1);

        assertThat(members.size()).isEqualTo(2);
        assertThat(limitedMembers.size()).isEqualTo(1);
    }

    @Test
    void update() {
        String originalPassword = "test_password";
        String changedPassword = "updated_test_password";
        Member member = Member.builder()
                .userId("test_userId")
                .password(originalPassword)
                .name("test_name")
                .build();
        Member newMember = memberRepository.insert(member);

        assertThat(newMember).isEqualTo(member);

        ReflectionTestUtils.setField(
                newMember,
                "password",
                changedPassword
        );
        Member updatedMember = memberRepository.update(newMember);

        assertThat(updatedMember.getPassword()).isNotEqualTo(originalPassword);
        assertThat(updatedMember.getPassword()).isEqualTo(changedPassword);
    }
}