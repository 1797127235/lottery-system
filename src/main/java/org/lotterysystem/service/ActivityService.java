package org.lotterysystem.service;

import org.lotterysystem.controller.param.CreateActivityParam;
import org.lotterysystem.controller.param.PageParam;
import org.lotterysystem.service.dto.ActivityDTO;
import org.lotterysystem.service.dto.ActivityDetailDTO;
import org.lotterysystem.service.dto.CreateActivityDTO;
import org.lotterysystem.service.dto.PageListDTO;

public interface ActivityService {
    /*
        创建活动
        @param param
     */
    CreateActivityDTO createActivity(CreateActivityParam param);

    /*
        翻页查询活动列表
     */
    PageListDTO<ActivityDTO> findActivityList(PageParam pageParam);

    /*
        获取活动详细属性
     */
    ActivityDetailDTO getActivityDetail(Long activityId);
}
