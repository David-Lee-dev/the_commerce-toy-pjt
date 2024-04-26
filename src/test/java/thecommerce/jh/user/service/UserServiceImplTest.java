package thecommerce.jh.user.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
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

            when(userRepository.insert(newUser)).thenReturn(newUser);
            User createdUser = userService.createUser(newUser);

            assertThat(createdUser).isEqualTo(newUser);
        }

        @Test
        @DisplayName("중복 user data 대해 오류 발생")
        void userIdViolation() {
            List<User> returnValueOfRepository = new ArrayList<>();
            returnValueOfRepository.add(existedUser);
            User duplicatedUserId = User.builder().userId(existedUser.getUserId()).build();

            when(userRepository.insert(any(User.class))).thenThrow(DataIntegrityViolationException.class);

            CustomException exception = assertThrows(CustomException.class, () -> userService.createUser(duplicatedUserId));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_USER_DATA);
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

            when(userRepository.findByUserId(anyString())).thenReturn(Optional.ofNullable(user));
            when(userRepository.update(any(User.class))).thenReturn(userUpdated);

            User result = userService.updateUser(userUpdated);

            assertThat(result).isEqualTo(userUpdated);
        }

        @Test
        @DisplayName("존재하지 않는 User일 경우 오류 발생")
        void noExistedResourceViolation() {
            User user = TestUserBuilder.build();
            when(userRepository.findByUserId(anyString())).thenReturn(Optional.ofNullable(null));

            Assertions.assertThrows(CustomException.class, () -> userService.updateUser(user));
        }

        @Test
        @DisplayName("중복 user data 대해 오류 발생")
        void userIdViolation() {
            User user = TestUserBuilder.build();
            User userUpdated = TestUserBuilder.build("test_userId", "test_password", "new_name", "new_nickname", "010-1111-1111", "new@test.com");

            when(userRepository.findByUserId(anyString())).thenReturn(Optional.ofNullable(user));
            when(userRepository.update(any(User.class))).thenThrow(DataIntegrityViolationException.class);

            CustomException exception = assertThrows(CustomException.class, () -> userService.updateUser(userUpdated));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_USER_DATA);
        }
    }
}