package thecommerce.jh.user.dto;

import lombok.Getter;
import thecommerce.jh.user.model.User;

@Getter
public class UserUpdateDto {
    private String name;

    private String nickname;

    private String phoneNumber;

    private String email;

    public User toEntity(String userId) {
        return User.builder()
                .userId(userId)
                .name(this.getName())
                .nickname(this.getNickname())
                .phoneNumber(this.getPhoneNumber())
                .email(this.getEmail())
                .build();
    }
}
