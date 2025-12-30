package org.lotterysystem.dao.dataobject;


import lombok.Data;

@Data
public class ActivityDO extends BaseDo{
    /*
        活动名称
     */
    private String activityName;

    /*
        活动描述
     */
    private String description;

    /*
        活动状态
     */
    private String status;

}
