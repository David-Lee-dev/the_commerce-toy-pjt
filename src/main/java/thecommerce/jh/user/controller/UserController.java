package thecommerce.jh.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import thecommerce.jh.user.dto.UserCreateDto;
import thecommerce.jh.user.dto.UserUpdateDto;

public interface UserController {
    ResponseEntity join(@RequestBody UserCreateDto userCreateDto);

    ResponseEntity list(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "createdAt") String sort
    );

    ResponseEntity modify(
            @PathVariable("userId") String userId,
            @RequestBody UserUpdateDto userUpdateDto
    );
}
