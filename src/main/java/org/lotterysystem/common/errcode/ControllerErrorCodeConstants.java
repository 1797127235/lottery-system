package org.lotterysystem.common.errcode;


/*
    控制端异常
 */
public interface ControllerErrorCodeConstants {
    //人员模块的错误码
    ErrorCode REGISTER_ERROR = new ErrorCode(100,"注册失败");
    ErrorCode LOGIN_ERROR = new ErrorCode(101,"登录失败");
    //活动模块错误码
    ErrorCode CREATE_ACTIVITY_ERROR = new ErrorCode(200,"创建活动失败");
    ErrorCode FIND_ACTIVITY_LIST_ERROR = new ErrorCode(201,"查找活动列表失败");
    ErrorCode GET_ACTIVITY_DETAIL_ERROR = new ErrorCode(202,"查询活动详细信息失败");
    //奖品模块错误码
    ErrorCode FIND_PRIZE_LIST_ERROR = new ErrorCode(300,"奖品列表查询失败");

    //抽奖模块错误码
}
