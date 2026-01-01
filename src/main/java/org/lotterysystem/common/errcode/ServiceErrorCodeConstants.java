package org.lotterysystem.common.errcode;

public interface ServiceErrorCodeConstants {
    //人员模块的错误码
    ErrorCode REGISTER_INFO_IS_EMPTY = new ErrorCode(100,"注册信息为空");
    ErrorCode REGISTER_EMAIL_INVALID = new ErrorCode(101,"邮箱格式不正确");
    ErrorCode REGISTER_PHONE_INVALID = new ErrorCode(102,"手机号格式不正确");
    ErrorCode REGISTER_PASSWORD_TOO_SHORT = new ErrorCode(103,"密码长度不足六位");
    ErrorCode IDENTITY_ERROR = new ErrorCode(104,"身份错误");
    ErrorCode MAIL_USED = new ErrorCode(105,"邮箱被使用");
    ErrorCode PHONE_USED = new ErrorCode(106,"手机号被使用");

    // 登录模块
    ErrorCode LOGIN_INFO_IS_EMPTY = new ErrorCode(110,"登录信息为空");
    ErrorCode LOGIN_NAME_OR_PASSWORD_EMPTY = new ErrorCode(111,"账号或密码为空");
    ErrorCode LOGIN_ACCOUNT_NOT_FOUND = new ErrorCode(112,"账号不存在");
    ErrorCode LOGIN_PASSWORD_ERROR = new ErrorCode(113,"密码错误");
    ErrorCode LOGIN_IDENTITY_MISMATCH = new ErrorCode(114,"身份不匹配");

    // 活动模块错误码
    ErrorCode CREATE_ACTIVITY_INFO_IS_EMPTY = new ErrorCode(115,"活动有效性为空");
    ErrorCode ACTIVITY_USER_ERROR = new ErrorCode(116,"创建关联人员异常");
    ErrorCode ACTIVITY_PRIZE_ERROR = new ErrorCode(117,"创建关联活动异常");
    ErrorCode USER_PRIZE_AMOUNT_ERROR = new ErrorCode(118,"活动关联的奖品或人员数量设置异常");
    ErrorCode ACTIVITY_PRIZE_TIERS_ERROR = new ErrorCode(119,"活动奖品等级设置错误");
    // 奖品模块错误码

    // 抽奖模块错误码

    // 图片错误码
    ErrorCode PIC_UPLOAD_ERROR = new ErrorCode(500,"图片上传失败");
}
