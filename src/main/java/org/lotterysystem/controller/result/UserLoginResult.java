package org.lotterysystem.controller.result;

import lombok.Data;
import org.lotterysystem.service.enums.UserIdentityEnum;

@Data
public class UserLoginResult {

    // JWT令牌
    private String token;
    // 登录人员身份
    private UserIdentityEnum identity;
}
