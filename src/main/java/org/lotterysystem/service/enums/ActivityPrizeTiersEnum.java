package org.lotterysystem.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityPrizeTiersEnum {
    FIRST_PRIZE(1,"一等奖"),
    SECOND_PRIZE(1,"二等奖"),
    THIRD_PRIZE(1,"三等奖");

    private Integer code;
    private String message;

    public static ActivityPrizeTiersEnum forName(String name) {
        for (ActivityPrizeTiersEnum tiersEnum : ActivityPrizeTiersEnum.values()) {
            if (tiersEnum.name().equals(name)) {
                return tiersEnum;
            }
        }
        return null;
    }
}
