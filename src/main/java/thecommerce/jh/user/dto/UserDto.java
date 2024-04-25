package thecommerce.jh.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import thecommerce.jh.user.model.User;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserDto {
    private Long id;

    private String name;

    private String nickname;

    private String phoneNumber;

    private String email;

    private LocalDateTime createdAt;

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
    }
}
