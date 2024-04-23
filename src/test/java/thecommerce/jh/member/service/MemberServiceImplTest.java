package thecommerce.jh.member.service;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import thecommerce.jh.member.enums.SortBy;
import thecommerce.jh.member.model.Member;
import thecommerce.jh.member.repository.MemberRepository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;

    @MockBean
    MemberRepository memberRepository;

    @Nested
    class CreateMember {

        Member existedMember = Member.builder()
                .userId("test_userId")
                .password("test_password")
                .name("test_name")
                .nickname("test_nickname")
                .phoneNumber("010-0000-0000")
                .email("test@test.com")
                .build();

        @Test
        @DisplayName("정상적으로 Member 생성")
        void idealCreation() {
            Member newMember =  Member.builder()
                    .userId("new_userId")
                    .password("new_password")
                    .name("new_name")
                    .nickname("new_nickname")
                    .phoneNumber("010-1111-1111")
                    .email("new@test.com")
                    .build();

            when(memberRepository.findByArguments(newMember)).thenReturn(new ArrayList<Member>());
            when(memberRepository.insert(newMember)).thenReturn(newMember);

            Member createdMember = memberService.createMember(newMember);
            assertThat(createdMember).isEqualTo(newMember);
        }

        @Test
        @DisplayName("동일한 정보를 가진 Member가 존재하는 경우 오류 발생")
        void nicknameViolation() {
            List<Member> returnValueOfRepository = new ArrayList<>();
            returnValueOfRepository.add(existedMember);

            when(memberRepository.findByArguments(existedMember)).thenReturn(returnValueOfRepository);

            assertThatThrownBy(() -> memberService.createMember(existedMember)).isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    class RetrieveMembers {

        @Captor
        private ArgumentCaptor<Integer> offsetCaptor;

        @Test
        @DisplayName("정상적으로 Member list 반환")
        void idealSearch() {
            List<Member> returnValueOfRepository = new ArrayList<>();
            returnValueOfRepository.add(new Member());
            returnValueOfRepository.add(new Member());
            returnValueOfRepository.add(new Member());

            when(memberRepository.findAll(anyInt(), anyInt(), any(SortBy.class), anyBoolean())).thenReturn(returnValueOfRepository);

            List<Member> members = memberService.retrieveMembers(0, 10, SortBy.CREATED_AT, true);
            assertThat(members.size()).isEqualTo(3);
        }

        @Test
        @DisplayName("offset 테스트")
        void offsetTest() {
            memberService.retrieveMembers(3, 12, SortBy.CREATED_AT, false);
            verify(memberRepository).findAll(offsetCaptor.capture(), anyInt(), any(SortBy.class), anyBoolean());
            assertThat(offsetCaptor.getValue()).isEqualTo(24);
        }
    }
}