package org.lotterysystem.dao.dataobject;

import lombok.Data;

@Data
public class ActivityPrizeDO extends BaseDo {
    /*
        活动id
     */
    private Long activityId;

    /*
        奖品id
     */
    private Long prizeId;

    /*
        奖品数量
     */
    private Long prizeAmount;

    /*
        奖品等级
     */
    private String prizeTiers;

    /*
        状态
     */
    private String status;
}
