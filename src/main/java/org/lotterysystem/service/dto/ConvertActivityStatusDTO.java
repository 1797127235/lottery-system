package org.lotterysystem.service.dto;

import lombok.Data;
import org.lotterysystem.service.enums.ActivityPrizeStatusEnum;
import org.lotterysystem.service.enums.ActivityStatusEnum;
import org.lotterysystem.service.enums.ActivityUserStatusEnum;

import java.util.List;

/*
    状态扭转
 */
@Data
public class ConvertActivityStatusDTO {
    /*
        活动id
     */
    private Long activityId;

    /*
        活动目标状态
     */
    private ActivityStatusEnum targetActivityStatus;

    /*
        奖品id
     */
    private Long prizeId;

    /*
        奖品目标状态
     */
    private ActivityPrizeStatusEnum targetPrizeStatus;

    /*
        人员id列表
     */
    private List<Long> userIds;

    /*
        人员目标状态
     */
    private ActivityUserStatusEnum targetUserStatus;

}
