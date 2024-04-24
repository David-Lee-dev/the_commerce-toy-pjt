package thecommerce.jh.user.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    UNKNOWN("000_UNKNOWN", "internal server error"),
    DUPLICATED_USER_ID("001_DUPLICATED_USER_ID", "user id is duplicated"),
    DUPLICATED_PHONE_NUMBER("002_DUPLICATED_PHONE_NUMBER", "phone number is duplicated"),
    DUPLICATED_EMAIL("003_DUPLICATED_EMAIL", "email is duplicated"),
    DUPLICATED_NICKNAME("004_DUPLICATED_NICKNAME", "nickname is duplicated"),
    NON_EXISTENT("005_NON_EXISTENT", "non-existent resource");

    private final String code;
    private final String msg;
}
