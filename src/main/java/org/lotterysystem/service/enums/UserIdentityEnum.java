package org.lotterysystem.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
    用户身份信息的枚举类
 */

@Getter
@AllArgsConstructor
public enum UserIdentityEnum {
    ADMIN("管理员"),
    NORMAL("普通用户");

    private final String message;

    public static UserIdentityEnum getUserIdentityForName(String message) {
        if (message == null) {
            return null;
        }
        for (UserIdentityEnum userIdentityEnum : UserIdentityEnum.values()) {
            // 兼容中文名称或枚举名（不区分大小写）
            if (userIdentityEnum.getMessage().equals(message)
                    || userIdentityEnum.name().equalsIgnoreCase(message)) {
                return userIdentityEnum;
            }
        }
        return null;
    }
}
