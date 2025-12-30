package org.lotterysystem.controller.result;

import lombok.Data;

import java.io.Serializable;

/*
    返回结果
 */

@Data
public class UserRegisterResult implements Serializable {
    private Long userId;
}
