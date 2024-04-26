package thecommerce.jh.user.common;

import thecommerce.jh.user.model.User;

public class TestUserBuilder {
    public static User build() {
        return User.builder()
                .userId("test_userId")
                .password("test_password")
                .name("test_name")
                .nickname("test_nickname")
                .phoneNumber("010-0000-0000")
                .email("test@test.com")
                .build();
    }

    public static User build(String userId, String password, String name) {
        return User.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .build();
    }

    public static User build(String userId, String password, String name, String nickname, String phoneNumber, String email) {
        return User.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .email(email)
                .build();

    }
}
