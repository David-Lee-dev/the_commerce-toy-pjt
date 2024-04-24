package thecommerce.jh.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPostDto {
    private String userId;

    private String password;

    private String passwordConfirm;

    private String name;

    private String nickname;

    private String phoneNumber;

    private String email;

}
