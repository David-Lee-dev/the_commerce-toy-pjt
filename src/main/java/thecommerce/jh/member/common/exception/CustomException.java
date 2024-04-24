package thecommerce.jh.member.common.exception;

import lombok.Getter;
import thecommerce.jh.member.common.enums.ErrorCode;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String detail;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.detail = "";
    }

    public CustomException(ErrorCode errorCode, Throwable cause) {
        this.errorCode = errorCode;
        this.detail = cause.getMessage();
    }
}