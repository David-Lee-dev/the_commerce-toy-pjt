package thecommerce.jh.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;
import thecommerce.jh.user.common.TestUserBuilder;
import thecommerce.jh.user.common.enums.SortBy;
import thecommerce.jh.user.config.AppConfig;
import thecommerce.jh.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Import({AppConfig.class, JpaUserRepository.class})
@DataJpaTest
class JpaUserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    void insert() {
        User user = TestUserBuilder.build();

        userRepository.insert(user);

        User createdUser = entityManager.find(User.class, user.getId());
        assertThat(createdUser).isEqualTo(user);
    }

    @Test
    void findById() {
        User user = TestUserBuilder.build();
        entityManager.persist(user);

        User foundUser = userRepository.findById(user.getId()).get();

        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void findByUserId() {
        User user = TestUserBuilder.build();
        entityManager.persist(user);

        User foundUser = userRepository.findByUserId(user.getUserId()).get();

        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void findByArguments() {
        User user = TestUserBuilder.build();
        userRepository.insert(user);

        List<User> foundByUserId = userRepository.findByArguments(User.builder().userId(user.getUserId()).build());
        List<User> foundByNickname = userRepository.findByArguments(User.builder().nickname(user.getNickname()).build());
        List<User> foundByPhoneNumber = userRepository.findByArguments(User.builder().phoneNumber(user.getPhoneNumber()).build());
        List<User> foundByEmail = userRepository.findByArguments(User.builder().email(user.getEmail()).build());
        List<User> foundByMultiArguments = userRepository.findByArguments(User.builder().phoneNumber(user.getPhoneNumber()).email(user.getEmail()).build());

        assertThat(foundByUserId.size()).isEqualTo(1);
        assertThat(foundByNickname.size()).isEqualTo(1);
        assertThat(foundByPhoneNumber.size()).isEqualTo(1);
        assertThat(foundByEmail.size()).isEqualTo(1);
        assertThat(foundByMultiArguments.size()).isEqualTo(1);
    }

    @Test
    void findAll() {
        User userA = TestUserBuilder.build("test_userId_A", "test_password_A", "test_name_A");
        User userB = TestUserBuilder.build("test_userId_B", "test_password_B", "test_name_B");
        userRepository.insert(userA);
        userRepository.insert(userB);

        List<User> users = userRepository.findAll(0, 10, null, false);
        List<User> usersOrderedByCreatedAtDesc = userRepository.findAll(0, 10, SortBy.CREATED_AT, true);
        List<User> usersLimited = userRepository.findAll(1, 1, null, false);

        assertThat(users.size()).isEqualTo(2);
        assertThat(usersOrderedByCreatedAtDesc.get(0)).isEqualTo(userB);
        assertThat(usersOrderedByCreatedAtDesc.get(1)).isEqualTo(userA);
        assertThat(usersLimited.size()).isEqualTo(1);
        assertThat(usersLimited.get(0)).isEqualTo(userB);
    }

    @Test
    void update() {
        String originalPassword = "test_password";
        String changedPassword = "updated_test_password";
        User user = TestUserBuilder.build("test_userId", originalPassword, "test_name");
        userRepository.insert(user);

        assertThat(user.getPassword()).isEqualTo(originalPassword);

        User userForUpdate = TestUserBuilder.build("test_userId", changedPassword, "test_name");
        ReflectionTestUtils.setField(userForUpdate,"id", user.getId());
        User updatedUser = userRepository.update(userForUpdate);

        assertThat(updatedUser.getPassword()).isNotEqualTo(originalPassword);
        assertThat(updatedUser.getPassword()).isEqualTo(changedPassword);
    }
}