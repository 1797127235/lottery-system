package org.lotterysystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.lotterysystem.common.errcode.ControllerErrorCodeConstants;
import org.lotterysystem.common.exception.ControllerException;
import org.lotterysystem.common.pojo.CommonResult;
import org.lotterysystem.controller.param.CreateActivityParam;
import org.lotterysystem.controller.result.CreateActivityResult;
import org.lotterysystem.service.ActivityService;
import org.lotterysystem.service.dto.CreateActivityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @RequestMapping("/activoty/create")
    public CommonResult<CreateActivityResult> createActivity(@Validated @RequestBody CreateActivityParam param) {
        CreateActivityDTO activity = activityService.createActivity(param);

        return CommonResult.success(convertToCreateActivityResult(activity));

    }

    private CreateActivityResult convertToCreateActivityResult(CreateActivityDTO activity) {
        if(null == activity) {
            throw new ControllerException(ControllerErrorCodeConstants.CREATE_ACTIVITY_ERROR);
        }
        CreateActivityResult result = new CreateActivityResult();

        result.setId(activity.getId());

        return  result;

    }
}
