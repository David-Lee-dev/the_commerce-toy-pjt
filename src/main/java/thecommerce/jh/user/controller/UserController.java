package thecommerce.jh.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thecommerce.jh.user.common.enums.ErrorCode;
import thecommerce.jh.user.common.exception.CustomException;
import thecommerce.jh.user.dto.UserPostDto;
import thecommerce.jh.user.model.User;
import thecommerce.jh.user.service.UserService;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody UserPostDto userPostDto) {
        if(!userPostDto.getPassword().equals(userPostDto.getPasswordConfirm())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        User user = User.builder()
                .userId(userPostDto.getUserId())
                .password(userPostDto.getPassword())
                .name(userPostDto.getName())
                .nickname(userPostDto.getNickname())
                .phoneNumber(userPostDto.getPhoneNumber())
                .email(userPostDto.getEmail())
                .build();

        userService.createUser(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
