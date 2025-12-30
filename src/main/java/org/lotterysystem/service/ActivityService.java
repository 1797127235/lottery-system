package org.lotterysystem.service;

import org.lotterysystem.controller.param.CreateActivityParam;
import org.lotterysystem.service.dto.CreateActivityDTO;

public interface ActivityService {
    /*
        创建活动
        @param param
     */
    CreateActivityDTO createActivity(CreateActivityParam param);
}
