package org.lotterysystem.service.activitystatus.operator;

import org.lotterysystem.dao.dataobject.ActivityDO;
import org.lotterysystem.dao.mapper.ActivityMapper;
import org.lotterysystem.dao.mapper.ActivityPrizeMapper;
import org.lotterysystem.service.dto.ConvertActivityStatusDTO;
import org.lotterysystem.service.enums.ActivityPrizeStatusEnum;
import org.lotterysystem.service.enums.ActivityStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivityOperator extends AbstructActivityOperator {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;

    @Override
    public Integer sequence() {
        return 2;
    }

    @Override
    public Boolean needConvert(ConvertActivityStatusDTO convertActivityStatusDTO) {
        Long activityId = convertActivityStatusDTO.getActivityId();
        ActivityStatusEnum targetActivityStatus = convertActivityStatusDTO.getTargetActivityStatus();
        if(null == activityId || null == targetActivityStatus) {
            return false;
        }

        ActivityDO activityDO = activityMapper.selectById(activityId);
        if(null == activityDO) {
            return false;
        }

        // 当前活动状态与传入的状态一致就不用处理
        if(targetActivityStatus.name().equalsIgnoreCase(activityDO.getStatus())) {
            return false;
        }

        // 奖品是否全部抽完（统计仍处于初始化/可抽状态的奖品数量）
        int runningPrizeCount = activityPrizeMapper.countPrizeByStatus(activityId, ActivityPrizeStatusEnum.INIT.name());
        // 如果目标状态是已完成，但还有未抽完的奖品，则暂不转换
        if (ActivityStatusEnum.COMPLETED == targetActivityStatus && runningPrizeCount > 0) {
            return false;
        }

        return true;
    }

    @Override
    public Boolean convert(ConvertActivityStatusDTO convertActivityStatusDTO) {
        try {
            activityMapper.updateStatus(convertActivityStatusDTO.getActivityId(),convertActivityStatusDTO.getTargetActivityStatus().name());
            return true;
        }catch (Exception e) {
            return false;
        }
    }

}
