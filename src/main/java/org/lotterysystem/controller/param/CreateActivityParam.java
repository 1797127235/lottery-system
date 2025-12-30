package org.lotterysystem.controller.param;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateActivityParam {
    /*
        活动名称
     */
    @NotBlank(message = "活动名称不能为空")
    private String activityName;

    /*
        活动描述
     */
    @NotBlank(message = "活动描述不能为空")
    private String description;

    /*
        活动关联的奖品
     */
    @NotEmpty(message = "活动奖品不能为空")
    @Valid
    List<CreatePrizeByActivityParam> activityPrizeList;

    /*
        活动关联的人员
     */
    @NotEmpty(message = "活动关联人员不能为空")
    @Valid
    List<CreateUserByActivityParam> activityUserList;
}
