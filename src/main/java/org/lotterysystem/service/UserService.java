package org.lotterysystem.service;

import org.lotterysystem.controller.param.UserPasswordLoginParam;
import org.lotterysystem.controller.param.UserRegisterParam;
import org.lotterysystem.service.dto.UserDTO;
import org.lotterysystem.service.dto.UserLoginDTO;
import org.lotterysystem.service.dto.UserRegisterDTO;
import org.lotterysystem.service.enums.UserIdentityEnum;

import java.util.List;

public interface UserService {
    /*
        注册
     */
    UserRegisterDTO register(UserRegisterParam  param);

    /*
        登录
     */
    UserLoginDTO login(UserPasswordLoginParam param);

    /*
        根据身份查人员列表
     */
    List<UserDTO> findUserInfo(UserIdentityEnum identity);
}
