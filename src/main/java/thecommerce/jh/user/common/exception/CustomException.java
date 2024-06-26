package thecommerce.jh.user.common.exception;

import lombok.Getter;
import thecommerce.jh.user.common.enums.ErrorCode;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String detail;

    public CustomException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
        this.detail = "";
    }

    public CustomException(ErrorCode errorCode, String detail) {
        super(detail);
        this.errorCode = errorCode;
        this.detail = detail;
    }

    public CustomException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.detail = cause.getMessage();
    }
}
