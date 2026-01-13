package org.lotterysystem.service.activitystatus.operator;


import org.lotterysystem.dao.dataobject.ActivityPrizeDO;
import org.lotterysystem.dao.mapper.ActivityPrizeMapper;
import org.lotterysystem.service.dto.ConvertActivityStatusDTO;
import org.lotterysystem.service.enums.ActivityPrizeStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrizeOperator extends AbstructActivityOperator {
    @Override
    public Integer sequence() {
        return 1;
    }

    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;

    @Override
    public Boolean needConvert(ConvertActivityStatusDTO convertActivityStatusDTO) {
        Long activityId = convertActivityStatusDTO.getActivityId();
        Long prizeId = convertActivityStatusDTO.getPrizeId();
        ActivityPrizeStatusEnum targetPrizeStatus =  convertActivityStatusDTO.getTargetPrizeStatus();
        if(null ==  targetPrizeStatus || null == prizeId){
            return false;
        }

        ActivityPrizeDO activityPrizeDO = activityPrizeMapper.selectByAPId(activityId, prizeId);

        if(null == activityPrizeDO){
            return false;
        }

        // 当前奖品状态和目标状态如果一致不更新
        if(targetPrizeStatus.name().equalsIgnoreCase(activityPrizeDO.getStatus())) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean convert(ConvertActivityStatusDTO convertActivityStatusDTO) {
        try {
            Long activityId = convertActivityStatusDTO.getActivityId();
            Long prizeId = convertActivityStatusDTO.getPrizeId();
            ActivityPrizeStatusEnum targetPrizeStatus =  convertActivityStatusDTO.getTargetPrizeStatus();
            activityPrizeMapper.updateStatus(activityId, prizeId, targetPrizeStatus.name());
            return true;
        } catch(Exception e){
            return false;
        }
    }
}
