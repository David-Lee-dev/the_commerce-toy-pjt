package thecommerce.jh.user.service;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import thecommerce.jh.user.common.enums.ErrorCode;
import thecommerce.jh.user.common.enums.SortBy;
import thecommerce.jh.user.common.exception.CustomException;
import thecommerce.jh.user.model.User;
import thecommerce.jh.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Nested
    class CreateUser {

        User existedUser = User.builder()
                .userId("test_userId")
                .password("test_password")
                .name("test_name")
                .nickname("test_nickname")
                .phoneNumber("010-0000-0000")
                .email("test@test.com")
                .build();

        @Test
        @DisplayName("정상적으로 User 생성")
        void idealCreation() {
            User newUser =  User.builder()
                    .userId("new_userId")
                    .password("new_password")
                    .name("new_name")
                    .nickname("new_nickname")
                    .phoneNumber("010-1111-1111")
                    .email("new@test.com")
                    .build();

            when(userRepository.findByArguments(newUser)).thenReturn(new ArrayList<User>());
            when(userRepository.insert(newUser)).thenReturn(newUser);

            User createdUser = userService.createUser(newUser);
            assertThat(createdUser).isEqualTo(newUser);
        }

        @Test
        @DisplayName("중복 userId에 대해 오류 발생")
        void userIdViolation() {
            List<User> returnValueOfRepository = new ArrayList<>();
            returnValueOfRepository.add(existedUser);
            User duplicatedUserId = User.builder().userId(existedUser.getUserId()).build();

            when(userRepository.findByArguments(any(User.class))).thenReturn(returnValueOfRepository);

            try {
                userService.createUser(duplicatedUserId);
            } catch(CustomException e) {
                assertThat(e).isInstanceOf(CustomException.class);
                assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_USER_ID);
            }

        }

        @Test
        @DisplayName("중복 phoneNumber에 대해 오류 발생")
        void phoneNumberViolation() {
            List<User> returnValueOfRepository = new ArrayList<>();
            returnValueOfRepository.add(existedUser);
            User duplicatedUserId = User.builder().phoneNumber(existedUser.getPhoneNumber()).build();

            when(userRepository.findByArguments(any(User.class))).thenReturn(returnValueOfRepository);

            try {
                userService.createUser(duplicatedUserId);
            } catch(CustomException e) {
                assertThat(e).isInstanceOf(CustomException.class);
                assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_PHONE_NUMBER);
            }

        }

        @Test
        @DisplayName("중복 email 대해 오류 발생")
        void emailViolation() {
            List<User> returnValueOfRepository = new ArrayList<>();
            returnValueOfRepository.add(existedUser);
            User duplicatedUserId = User.builder().email(existedUser.getEmail()).build();

            when(userRepository.findByArguments(any(User.class))).thenReturn(returnValueOfRepository);

            try {
                userService.createUser(duplicatedUserId);
            } catch(CustomException e) {
                assertThat(e).isInstanceOf(CustomException.class);
                assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_EMAIL);
            }

        }

        @Test
        @DisplayName("중복 nickname에 대해 오류 발생")
        void nicknameViolation() {
            List<User> returnValueOfRepository = new ArrayList<>();
            returnValueOfRepository.add(existedUser);
            User duplicatedUserId = User.builder().nickname(existedUser.getNickname()).build();

            when(userRepository.findByArguments(any(User.class))).thenReturn(returnValueOfRepository);

            try {
                userService.createUser(duplicatedUserId);
            } catch(CustomException e) {
                assertThat(e).isInstanceOf(CustomException.class);
                assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_NICKNAME);
            }

        }
    }

    @Nested
    class RetrieveUsers {

        @Captor
        private ArgumentCaptor<Integer> offsetCaptor;

        @Test
        @DisplayName("정상적으로 User list 반환")
        void idealSearch() {
            List<User> returnValueOfRepository = new ArrayList<>();
            returnValueOfRepository.add(new User());
            returnValueOfRepository.add(new User());
            returnValueOfRepository.add(new User());

            when(userRepository.findAll(anyInt(), anyInt(), any(SortBy.class), anyBoolean())).thenReturn(returnValueOfRepository);

            List<User> users = userService.retrieveUsers(0, 10, SortBy.CREATED_AT, true);

            assertThat(users.size()).isEqualTo(3);
        }

        @Test
        @DisplayName("offset 테스트")
        void offsetTest() {
            userService.retrieveUsers(3, 12, SortBy.CREATED_AT, false);
            verify(userRepository).findAll(offsetCaptor.capture(), anyInt(), any(SortBy.class), anyBoolean());
            assertThat(offsetCaptor.getValue()).isEqualTo(24);
        }
    }

    @Nested
    class UpdateUser {

        @Test
        @DisplayName("정상적으로 데이터 업데이트")
        void idealUpdate() {
            User user = User.builder().id(1L).build();
            User userUpdated = User.builder()
                    .id(1L)
                    .password("test_password")
                    .build();

            when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
            when(userRepository.update(userUpdated)).thenReturn(userUpdated);

            User result = userService.updateUser(userUpdated);

            assertThat(result).isEqualTo(userUpdated);
        }

        @Test
        @DisplayName("존재하지 않는 User일 경우 오류 발생")
        void noExistedResourceViolation() {
            when(userRepository.findById(100L)).thenReturn(null);

            try {
                userService.updateUser(new User());
            } catch(CustomException e) {
                assertThat(e).isInstanceOf(CustomException.class);
                assertThat(e.getErrorCode()).isEqualTo(ErrorCode.NON_EXISTENT);
            }
        }
    }
}