package org.lotterysystem.dao.dataobject;

import lombok.Data;

@Data
public class ActivityUserDO extends BaseDo {
    /*
        活动id
     */
    private Long activityId;

    /*
        用户id
     */
    private Long userId;

    /*
        用户姓名
     */
    private String userName;

    /*
        状态
     */
    private String status;
}
