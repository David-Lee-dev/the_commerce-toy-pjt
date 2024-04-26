package thecommerce.jh.user.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "000_UNKNOWN", "internal server error"),
    DUPLICATED_USER_DATA(HttpStatus.BAD_REQUEST, "001_DUPLICATED_USER_DATA", "user data is duplicated"),
    NON_EXISTENT(HttpStatus.NOT_FOUND, "005_NON_EXISTENT", "non-existent resource"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "006_INVALID_PASSWORD", "password not matched"),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "007_MISSING_PARAMETER", "missing parameter"),
    INVALID_PAGINATION(HttpStatus.BAD_REQUEST, "008_INVALID_PAGINATION", "invalid pagination");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
