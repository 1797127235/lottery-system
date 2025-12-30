package org.lotterysystem.controller.handler;

import org.lotterysystem.common.errcode.GlobalErrorCodeConstants;
import org.lotterysystem.common.exception.ControllerException;
import org.lotterysystem.common.exception.ServiceException;
import org.lotterysystem.common.pojo.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.rmi.ServerException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = ServiceException.class)
    public CommonResult<?> serviceexception(ServiceException e) {
        //打错误日志
        logger.error("serviceException: ",e);
        //构造错误返回结果
        return CommonResult.error(
                GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(),
                e.getMessage()
        );
    }

    @ExceptionHandler(value = ControllerException.class)
    public CommonResult<?> controllerexception(ControllerException e) {
        //打错误日志
        logger.error("controllerException: ",e);
        //构造错误返回结果
        return CommonResult.error(
                GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(),
                e.getMessage()
        );
    }


    @ExceptionHandler(value = Exception.class)
    public CommonResult<?> exception(Exception e) {
        //打错误日志
        logger.error("服务异常", e);
        //构造错误返回结果
        return CommonResult.error(
                GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR
        );
    }
}
