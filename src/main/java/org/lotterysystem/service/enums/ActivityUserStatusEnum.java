package org.lotterysystem.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityUserStatusEnum {
    INIT(1,"初始化"),
    COMPLETED(2,"已被抽取");

    private Integer code;
    private String message;

    public static ActivityUserStatusEnum forName(String name) {
        for (ActivityUserStatusEnum item : ActivityUserStatusEnum.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return null;
    }
}
