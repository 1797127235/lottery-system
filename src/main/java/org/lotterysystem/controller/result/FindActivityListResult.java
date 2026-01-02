package org.lotterysystem.controller.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FindActivityListResult implements Serializable {
    /*
        总量
     */
    private Integer total;

    /*
        当前列表
     */
    private List<ActivityInfo> records;

    @Data
    public static class ActivityInfo implements Serializable {
        private Integer activityId;

        private String activityName;

        private String description;

        private Boolean valid;
    }
}
