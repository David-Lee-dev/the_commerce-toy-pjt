package thecommerce.jh.member.repository;

import org.junit.jupiter.api.DisplayName;
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
        List<Member> members = memberRepository.findAll();

        assertThat(members.size()).isEqualTo(2);
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