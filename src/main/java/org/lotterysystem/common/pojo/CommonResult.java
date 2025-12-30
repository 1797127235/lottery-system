package org.lotterysystem.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.lotterysystem.common.errcode.ErrorCode;
import org.lotterysystem.common.errcode.GlobalErrorCodeConstants;
import org.springframework.util.Assert;

import java.io.Serializable;

/*
    统一返回对象
 */
@AllArgsConstructor
@Data
public class CommonResult<T> implements Serializable {
    /*
        返回错误码
     */
    private Integer code;

    /*
        返回错误码描述
     */
    private String message;

    /*
        返回数据
     */
    private T data;

    public CommonResult() {

    }

    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.code = GlobalErrorCodeConstants.SUCCESS.getCode();
        result.data = data;
        result.message = GlobalErrorCodeConstants.SUCCESS.getMsg();
        return result;
    }

    public static <T> CommonResult<T> error(Integer code, String message) {
        Assert.isTrue(!GlobalErrorCodeConstants.SUCCESS.getCode().equals(code), "code不是错误的异常码");

        CommonResult<T> result = new CommonResult<>();
        result.code = code;
        result.message = message;

        return result;
    }

    public static <T> CommonResult<T> error(ErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMsg());
    }



}
