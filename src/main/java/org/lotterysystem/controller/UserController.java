package org.lotterysystem.controller;


import lombok.extern.slf4j.Slf4j;
import org.lotterysystem.common.errcode.ControllerErrorCodeConstants;
import org.lotterysystem.common.exception.ControllerException;
import org.lotterysystem.common.pojo.CommonResult;
import org.lotterysystem.common.utils.JacksonUtil;
import org.lotterysystem.controller.param.UserPasswordLoginParam;
import org.lotterysystem.controller.param.UserRegisterParam;
import org.lotterysystem.controller.result.BaseUserInfoResult;
import org.lotterysystem.controller.result.UserLoginResult;
import org.lotterysystem.controller.result.UserRegisterResult;
import org.lotterysystem.service.UserService;
import org.lotterysystem.service.dto.UserDTO;
import org.lotterysystem.service.dto.UserLoginDTO;
import org.lotterysystem.service.dto.UserRegisterDTO;
import org.lotterysystem.service.enums.UserIdentityEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    /*
        注册
     */

    @PostMapping("/register")
    public CommonResult<UserRegisterResult> userRegister(@Validated @RequestBody  UserRegisterParam param) {
        //日志打印
        logger.info("userRegister param:{}", JacksonUtil.writeValueAsString(param));

        // 调用service层服务进行访问
        UserRegisterDTO registerdto = userService.register(param);


        return CommonResult.success(convertToUserRegisterResult(registerdto));
    }


    /*
        密码登录
     */
    @PostMapping("/password/login")
    public CommonResult<UserLoginResult> userPasswordLogin(@Validated @RequestBody UserPasswordLoginParam param) {
        logger.info("UserPasswordLogin param:{}", JacksonUtil.writeValueAsString(param));

        UserLoginDTO userLoginDTO = userService.login(param);

        return CommonResult.success(convertToUserLoginResult(userLoginDTO));
    }


    @RequestMapping("/base-user/find-list")
    public CommonResult<List<BaseUserInfoResult>> findBaseUserInfo(String identity) {
        logger.info("findBaseUserInfo param:{}", JacksonUtil.writeValueAsString(identity));
        UserIdentityEnum identityEnum = UserIdentityEnum.getUserIdentityForName(identity);
        List<UserDTO> userDTOList = userService.findUserInfo(identityEnum);
        List<BaseUserInfoResult> resultList = convertToBaseUserInfoResultList(userDTOList);
        return CommonResult.success(resultList);
    }




    /*
    转换DTO层上传来的结果
    */
    private UserRegisterResult convertToUserRegisterResult(UserRegisterDTO registerdto) {
        UserRegisterResult userRegisterResult = new UserRegisterResult();
        if(null ==  registerdto) {
            throw new ControllerException(ControllerErrorCodeConstants.REGISTER_ERROR);
        }

        userRegisterResult.setUserId(registerdto.getUserId());

        return userRegisterResult;
    }


    private  UserLoginResult convertToUserLoginResult(UserLoginDTO userLoginDTO) {
        UserLoginResult userLoginResult = new UserLoginResult();
        if (null ==  userLoginDTO) {
            throw new ControllerException(ControllerErrorCodeConstants.LOGIN_ERROR);
        }

        userLoginResult.setToken(userLoginDTO.getToken());
        userLoginResult.setIdentity(userLoginDTO.getIdentity());

        return userLoginResult;
    }

    private List<BaseUserInfoResult> convertToBaseUserInfoResultList(List<UserDTO> dtoList) {
        if (dtoList == null) {
            return null;
        }
        return dtoList.stream().map(dto -> {
            BaseUserInfoResult r = new BaseUserInfoResult();
            r.setUserName(dto.getUserName());
            r.setIdentity(dto.getIdentity() == null ? null : dto.getIdentity().name());
            return r;
        }).collect(Collectors.toList());
    }

}
