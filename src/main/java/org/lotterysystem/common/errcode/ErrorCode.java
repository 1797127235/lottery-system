package org.lotterysystem.common.errcode;


import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class ErrorCode {
    //错误码
    private final Integer code;
    //错误分类
    private final String msg;

}
