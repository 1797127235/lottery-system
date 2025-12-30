package org.lotterysystem.service.impl;

import org.lotterysystem.common.errcode.ServiceErrorCodeConstants;
import org.lotterysystem.common.exception.ServiceException;
import org.lotterysystem.common.utils.JwtUtil;
import org.lotterysystem.common.utils.RegexUtil;
import org.lotterysystem.controller.param.UserPasswordLoginParam;
import org.lotterysystem.controller.param.UserRegisterParam;
import org.lotterysystem.dao.dataobject.Encrypt;
import org.lotterysystem.dao.dataobject.UserDo;
import org.lotterysystem.dao.mapper.UserMapper;
import org.lotterysystem.service.UserService;
import org.lotterysystem.service.dto.UserDTO;
import org.lotterysystem.service.dto.UserLoginDTO;
import org.lotterysystem.service.dto.UserRegisterDTO;
import org.lotterysystem.service.enums.UserIdentityEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import cn.hutool.crypto.digest.DigestUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    // 用户注册功能
    @Override
    public UserRegisterDTO register(UserRegisterParam param) {
        // 校验注册信息
        checkRegisterInfo(param);

        // 加密私密数据
        UserDo userDo = new UserDo();
        userDo.setUserName(param.getName());
        userDo.setEmail(param.getMail());
        userDo.setPhoneNumber(new Encrypt(param.getPhoneNumber()));
        userDo.setIdentity(param.getIdentity());
        if(StringUtils.hasText(param.getPassword())) {
            userDo.setPassword(DigestUtil.sha256Hex(param.getPassword()));
        }

        // 保存数据
        userMapper.insert(userDo);

        // 构造返回
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUserId(userDo.getId());
        return userRegisterDTO;
    }



    /*
        用户登录功能
     */

    public UserLoginDTO login(UserPasswordLoginParam param) {
        if(param == null) {
            throw new ServiceException((ServiceErrorCodeConstants.LOGIN_INFO_IS_EMPTY));
        }
        if(!StringUtils.hasText(param.getLoginName()) ||  !StringUtils.hasText(param.getPassword())) {
            throw new ServiceException((ServiceErrorCodeConstants.LOGIN_INFO_IS_EMPTY));
        }

        String loginName = param.getLoginName();
        UserDo userDo = null;
        if(RegexUtil.isValidEmail(loginName)) {
            userDo = userMapper.selectByMail(loginName);
        } else if(RegexUtil.isValidPhone(loginName)) {
            userDo = userMapper.selectByPhone(new Encrypt(loginName));
        }

        if(userDo == null) {
            throw new ServiceException((ServiceErrorCodeConstants.LOGIN_INFO_IS_EMPTY));
        }

        String encryptedPwd = DigestUtil.sha256Hex(param.getPassword());

        if(!encryptedPwd.equals(userDo.getPassword())) {
            throw new ServiceException((ServiceErrorCodeConstants.LOGIN_PASSWORD_ERROR));
        }

        // 身份校验
        if (StringUtils.hasText(param.getMandatoryIdentity())
                && !param.getMandatoryIdentity().equals(userDo.getIdentity())) {
            throw new ServiceException(ServiceErrorCodeConstants.LOGIN_IDENTITY_MISMATCH);
        }

        Map<String,Object> map = new HashMap<>();
        map.put("identity", userDo.getIdentity());
        map.put("userName", userDo.getUserName());
        map.put("email", userDo.getEmail());

        String token = jwtUtil.genJwt(String.valueOf(userDo.getId()), map);

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setToken(token);
        userLoginDTO.setIdentity(UserIdentityEnum.getUserIdentityForName(userDo.getIdentity()));
        return userLoginDTO;


    }


    @Override
    public List<UserDTO> findUserInfo(UserIdentityEnum identity) {
        if (identity == null) {
            throw new ServiceException(ServiceErrorCodeConstants.IDENTITY_ERROR);
        }
        List<UserDo> list = userMapper.selectByIdentity(identity.name());
        List<UserDTO> dtoList = new ArrayList<>();
        if (list != null) {
            for (UserDo userDo : list) {
                UserDTO dto = new UserDTO();
                dto.setId(userDo.getId());
                dto.setUserName(userDo.getUserName());
                dto.setEmail(userDo.getEmail());
                dto.setPhoneNumber(userDo.getPhoneNumber() == null ? null : userDo.getPhoneNumber().getValue());
                dto.setIdentity(UserIdentityEnum.getUserIdentityForName(userDo.getIdentity()));
                dtoList.add(dto);
            }
        }
        return dtoList;
    }


    private void checkRegisterInfo(UserRegisterParam param) {
        if (param == null) {
            throw new ServiceException(ServiceErrorCodeConstants.REGISTER_INFO_IS_EMPTY);
        }

        // 校验邮箱格式
        if (!RegexUtil.isValidEmail(param.getMail())) {
            throw new ServiceException(ServiceErrorCodeConstants.REGISTER_EMAIL_INVALID);
        }

        // 校验手机号格式：1 开头 11 位数字
        if (!RegexUtil.isValidPhone(param.getPhoneNumber())) {
            throw new ServiceException(ServiceErrorCodeConstants.REGISTER_PHONE_INVALID);
        }


        // 校验身份信息
        if(null == UserIdentityEnum.getUserIdentityForName(param.getIdentity())) {
            throw new ServiceException(ServiceErrorCodeConstants.IDENTITY_ERROR);
        }

        // 密码校验，至少六位
        if (!RegexUtil.isValidPassword(param.getPassword())) {
            throw new ServiceException(ServiceErrorCodeConstants.REGISTER_PASSWORD_TOO_SHORT);
        }

        // 校验邮箱是否被使用
        if(checkMailUser(param.getMail())) {
            throw new ServiceException(ServiceErrorCodeConstants.MAIL_USED);
        }

        // 检验手机是否存在
        if(checkPhoneNumberUsed(param.getPhoneNumber())) {
            throw new ServiceException(ServiceErrorCodeConstants.PHONE_USED);
        }


    }

    // 校验邮箱是否存在
    private boolean checkMailUser(String mail) {
        int count = userMapper.countByMail(mail);

        return count > 0;
    }

    // 校验手机号是否被使用
    private boolean checkPhoneNumberUsed(String phoneNumber) {
        int coutn = userMapper.countByPhone(new Encrypt(phoneNumber));
        if(coutn == 0){
            return false;
        }

        return true;
    }
    
}