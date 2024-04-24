package thecommerce.jh.user.common.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import thecommerce.jh.user.common.enums.ErrorCode;

@Data
@Builder
public class ErrorResponseEntity {
    private int status;
    private String name;
    private String code;
    private String message;

    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(ErrorCode errorCode){
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .status(errorCode.getHttpStatus().value())
                        .name(errorCode.name())
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
}