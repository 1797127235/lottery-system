package org.lotterysystem.service.enums;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PrizeLevelEnum {
    FIRST("FIRST_PRIZE", "一等奖", 1),
    SECOND("SECOND_PRIZE", "二等奖", 2),
    THIRD("THIRD_PRIZE", "三等奖", 3);

    private final String code;   // 业务编码，可与前端/数据库交互
    private final String label;  // 展示名称
    private final int order;     // 排序，数值越小等级越高
}