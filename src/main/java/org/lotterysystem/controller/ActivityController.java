package org.lotterysystem.controller;
import lombok.extern.slf4j.Slf4j;
import org.lotterysystem.common.errcode.ControllerErrorCodeConstants;
import org.lotterysystem.common.exception.ControllerException;
import org.lotterysystem.common.pojo.CommonResult;
import org.lotterysystem.controller.param.CreateActivityParam;
import org.lotterysystem.controller.param.PageParam;
import org.lotterysystem.controller.result.CreateActivityResult;
import org.lotterysystem.controller.result.FindActivityListResult;
import org.lotterysystem.controller.result.GetActivityDetailResult;
import org.lotterysystem.service.ActivityService;
import org.lotterysystem.service.DrawPrizeService;
import org.lotterysystem.service.dto.ActivityDTO;
import org.lotterysystem.service.dto.ActivityDetailDTO;
import org.lotterysystem.service.dto.CreateActivityDTO;
import org.lotterysystem.service.dto.PageListDTO;
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


    @RequestMapping("/activity/create")

    public CommonResult<CreateActivityResult> createActivity(@Validated @RequestBody CreateActivityParam param) {
        CreateActivityDTO activity = activityService.createActivity(param);

        return CommonResult.success(convertToCreateActivityResult(activity));

    }


    @RequestMapping("/activity/find-list")
    public CommonResult<FindActivityListResult> findActivityList(PageParam pageParam) {
        log.info("findActivityList start");

        return CommonResult.success(convertToFindActivityListResult(activityService.findActivityList(pageParam)));
    }

    @RequestMapping("/activity-detail/find")
    public CommonResult<GetActivityDetailResult> getActivityDetail(Long activityId) {
        log.info("getActivityDetail start");

        ActivityDetailDTO activityDetail = activityService.getActivityDetail(activityId);

        return CommonResult.success(convertToGetActivityDetailResult(activityDetail));

    }

    private GetActivityDetailResult convertToGetActivityDetailResult(ActivityDetailDTO activityDetail) {
        if(null == activityDetail) {
            throw new ControllerException(ControllerErrorCodeConstants.GET_ACTIVITY_DETAIL_ERROR);
        }

        GetActivityDetailResult result = new GetActivityDetailResult();

        result.setActivityId(activityDetail.getActivityId());
        result.setName(activityDetail.getName());
        result.setDescription(activityDetail.getDescription());
        result.setValid(activityDetail.valid());
        result.setUserDTOList(
                activityDetail.getUserDTOList()
                        .stream()
                        .map(userDTO -> {
                            GetActivityDetailResult.User user = new GetActivityDetailResult.User();
                            user.setUserId(userDTO.getUserId());
                            user.setUserName(userDTO.getUserName());
                            user.setValid(userDTO.valid());
                            return user;
                        })
                        .toList()
        );
        result.setPrizeDTOList(
                activityDetail.getPrizeDTOList()
                        .stream()
                        .map(prizeDTO -> {
                            GetActivityDetailResult.Prize prize = new GetActivityDetailResult.Prize();
                            prize.setPrizeId(prizeDTO.getPrizeId());
                            prize.setName(prizeDTO.getName());
                            prize.setImageUrl(prizeDTO.getImageUrl());
                            prize.setPrice(prizeDTO.getPrice());
                            prize.setDescription(prizeDTO.getDescription());
                            prize.setPrizeTiersEnum(prizeDTO.getPrizeTiersEnum() != null ? prizeDTO.getPrizeTiersEnum().name() : null);
                            prize.setPrizeAmount(prizeDTO.getPrizeAmount());
                            prize.setValid(prizeDTO.valid());
                            return prize;
                        })
                        .toList()
        );

        return  result;
    }


    private CreateActivityResult convertToCreateActivityResult(CreateActivityDTO activity) {
        if(null == activity) {
            throw new ControllerException(ControllerErrorCodeConstants.CREATE_ACTIVITY_ERROR);
        }
        CreateActivityResult result = new CreateActivityResult();

        result.setId(activity.getId());

        return  result;
    }

    private FindActivityListResult convertToFindActivityListResult(PageListDTO<ActivityDTO> activityList) {
        if(null == activityList) {
            throw new ControllerException(ControllerErrorCodeConstants.FIND_ACTIVITY_LIST_ERROR);
        }
        FindActivityListResult result = new FindActivityListResult();
        result.setTotal(activityList.getTotal());
        result.setRecords(
                activityList.getRecords()
                        .stream()
                        .map(activityDTO -> {
                            FindActivityListResult.ActivityInfo activityInfo = new FindActivityListResult.ActivityInfo();
                            if (activityDTO.getActivityId() != null) {
                                activityInfo.setActivityId(activityDTO.getActivityId().intValue());
                            }
                            activityInfo.setActivityName(activityDTO.getActivityName());
                            activityInfo.setDescription(activityDTO.getDescription());
                            activityInfo.setValid(activityDTO.valid());
                            return activityInfo;
                        })
                        .toList()
        );
        return result;
    }
}
