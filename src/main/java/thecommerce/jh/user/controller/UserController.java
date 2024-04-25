package thecommerce.jh.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thecommerce.jh.user.common.enums.ErrorCode;
import thecommerce.jh.user.common.enums.SortBy;
import thecommerce.jh.user.common.exception.CustomException;
import thecommerce.jh.user.dto.UserCreateDto;
import thecommerce.jh.user.dto.UserDto;
import thecommerce.jh.user.dto.UserUpdateDto;
import thecommerce.jh.user.model.User;
import thecommerce.jh.user.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/join")
    ResponseEntity join(@RequestBody UserCreateDto userCreateDto) {
        if(!userCreateDto.getPassword().equals(userCreateDto.getPasswordConfirm())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        userService.createUser(userCreateDto.toEntity());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/list")
    ResponseEntity list(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "createdAt") String sort
        ) {

        if(page <= 0 || pageSize <= 0) {
            throw new CustomException(ErrorCode.INVALID_PAGINATION);
        }

        SortBy sortBy = (sort == "createdAt") ? SortBy.CREATED_AT : SortBy.NAME;

        List<UserDto> users = userService.retrieveUsers(page, pageSize, sortBy, false)
                .stream()
                .map(user -> new UserDto(user))
                .collect(Collectors.toList());

        Map<String, Object> responseObject = new HashMap<>();
        responseObject.put("page", page);
        responseObject.put("total", users.size());
        responseObject.put("users", users);

        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PatchMapping("/{userId}")
    ResponseEntity modify(
            @PathVariable("userId") String userId,
            @RequestBody UserUpdateDto userUpdateDto
        ) {

        User user = userService.updateUser(userUpdateDto.toEntity(userId));

        return ResponseEntity.status(HttpStatus.OK).body(new UserDto(user));
    }

}
