package thecommerce.jh.user.service;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import thecommerce.jh.user.common.TestUserBuilder;
import thecommerce.jh.user.common.enums.ErrorCode;
import thecommerce.jh.user.common.enums.SortBy;
import thecommerce.jh.user.common.exception.CustomException;
import thecommerce.jh.user.model.User;
import thecommerce.jh.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Nested
    class CreateUser {

        User existedUser = TestUserBuilder.build();

        @Test
        @DisplayName("정상적으로 User 생성")
        void idealCreation() {
            User newUser = TestUserBuilder.build("new_userId", "new_password", "new_name", "new_nickname", "010-1111-1111", "new@test.com");

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

            CustomException exception = assertThrows(CustomException.class, () -> userService.createUser(duplicatedUserId));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_USER_ID);
        }

        @Test
        @DisplayName("중복 phoneNumber에 대해 오류 발생")
        void phoneNumberViolation() {
            List<User> returnValueOfRepository = new ArrayList<>();
            returnValueOfRepository.add(existedUser);
            User duplicatedUserId = User.builder().phoneNumber(existedUser.getPhoneNumber()).build();

            when(userRepository.findByArguments(any(User.class))).thenReturn(returnValueOfRepository);

            CustomException exception = assertThrows(CustomException.class, () -> userService.createUser(duplicatedUserId));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_PHONE_NUMBER);

        }

        @Test
        @DisplayName("중복 email 대해 오류 발생")
        void emailViolation() {
            List<User> returnValueOfRepository = new ArrayList<>();
            returnValueOfRepository.add(existedUser);
            User duplicatedUserId = User.builder().email(existedUser.getEmail()).build();

            when(userRepository.findByArguments(any(User.class))).thenReturn(returnValueOfRepository);

            CustomException exception = assertThrows(CustomException.class, () -> userService.createUser(duplicatedUserId));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_EMAIL);

        }

        @Test
        @DisplayName("중복 nickname에 대해 오류 발생")
        void nicknameViolation() {
            List<User> returnValueOfRepository = new ArrayList<>();
            returnValueOfRepository.add(existedUser);
            User duplicatedUserId = User.builder().nickname(existedUser.getNickname()).build();

            when(userRepository.findByArguments(any(User.class))).thenReturn(returnValueOfRepository);

            CustomException exception = assertThrows(CustomException.class, () -> userService.createUser(duplicatedUserId));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_NICKNAME);
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
            User user = TestUserBuilder.build();
            User userUpdated = TestUserBuilder.build("test_userId", "test_password", "new_name", "new_nickname", "010-1111-1111", "new@test.com");

            when(userRepository.findByArguments(any(User.class))).thenReturn(Arrays.asList(user));
            when(userRepository.update(any(User.class))).thenReturn(userUpdated);

            User result = userService.updateUser(userUpdated);

            assertThat(result).isEqualTo(userUpdated);
        }

        @Test
        @DisplayName("존재하지 않는 User일 경우 오류 발생")
        void noExistedResourceViolation() {
            when(userRepository.findByArguments(any(User.class))).thenReturn(new ArrayList<>());

            try {
                userService.updateUser(new User());
            } catch(CustomException e) {
                assertThat(e).isInstanceOf(CustomException.class);
                assertThat(e.getErrorCode()).isEqualTo(ErrorCode.NON_EXISTENT);
            }
        }
    }
}