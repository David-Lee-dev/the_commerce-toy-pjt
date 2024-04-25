package thecommerce.jh.user.dto;

import lombok.Getter;
import lombok.Setter;
import thecommerce.jh.user.model.User;

@Getter
public class UserCreationDto {
    private String userId;

    private String password;

    private String passwordConfirm;

    private String name;

    private String nickname;

    private String phoneNumber;

    private String email;

    public User toEntity() {
        return User.builder()
                .userId(this.getUserId())
                .password(this.getPassword())
                .name(this.getName())
                .nickname(this.getNickname())
                .phoneNumber(this.getPhoneNumber())
                .email(this.getEmail())
                .build();
    }
}
