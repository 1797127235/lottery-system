package org.lotterysystem.service.activitystatus.operator;

import org.lotterysystem.dao.mapper.ActivityUserMapper;
import org.lotterysystem.service.dto.ConvertActivityStatusDTO;
import org.lotterysystem.service.enums.ActivityUserStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class UserOperator extends AbstructActivityOperator {

    @Autowired
    private ActivityUserMapper activityUserMapper;

    @Override
    public Integer sequence() {
        return 1;
    }

    /*
        活动下的用户状态是否需要转换
     */
    @Override
    public Boolean needConvert(ConvertActivityStatusDTO convertActivityStatusDTO) {
        Long activityId = convertActivityStatusDTO.getActivityId();
        ActivityUserStatusEnum targetUserStatus = convertActivityStatusDTO.getTargetUserStatus();
        List<Long> userIds = convertActivityStatusDTO.getUserIds();
        if (activityId == null ||
                targetUserStatus == null ||
                CollectionUtils.isEmpty(userIds)) {
            return false;
        }


        return true;
    }

    @Override
    public Boolean convert(ConvertActivityStatusDTO convertActivityStatusDTO) {
        try {
            Long activityId = convertActivityStatusDTO.getActivityId();
            ActivityUserStatusEnum targetUserStatus = convertActivityStatusDTO.getTargetUserStatus();
            activityUserMapper.updateStatus(activityId, convertActivityStatusDTO.getUserIds(), targetUserStatus.name());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
