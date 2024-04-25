package thecommerce.jh.user.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortBy {
    CREATED_AT("createAt"),
    NAME("name");

    private final String value;
}
