package org.lotterysystem.service.activitystatus.impl;

import lombok.extern.slf4j.Slf4j;
import org.lotterysystem.common.errcode.ServiceErrorCodeConstants;
import org.lotterysystem.common.exception.ServiceException;
import org.lotterysystem.service.ActivityService;
import org.lotterysystem.service.activitystatus.ActivityStatusManager;
import org.lotterysystem.service.activitystatus.operator.AbstructActivityOperator;
import org.lotterysystem.service.dto.ConvertActivityStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Component
public class ActivityStatusManagerImpl implements ActivityStatusManager {

    @Autowired
    private final Map<String, AbstructActivityOperator> operatorMap = new HashMap<>();

    @Autowired
    private ActivityService activityService;

    /*
        处理状态扭转
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlerEvent(ConvertActivityStatusDTO convertActivityStatusDTO) {
        // 1 活动状态扭转有依赖性，导致代码维护性差
        // 2 状态扭转条件可能会扩展
        if(CollectionUtils.isEmpty(operatorMap)){
            log.warn("operatorMap is empty");
            return;
        }

        Map<String,AbstructActivityOperator> currMap = new HashMap<>(operatorMap);

        Boolean update1 = false;
        Boolean update2 = false;
        /*
            责任链模式
         */
        // 先处理人员 奖品
        update1 = processConvertStatus(convertActivityStatusDTO,currMap,1);
        // 后处理 活动
        update2 = processConvertStatus(convertActivityStatusDTO,currMap,2);
        // 更新缓存
        if(update1 || update2){
            activityService.cacheActivity(convertActivityStatusDTO.getActivityId());
        }

    }


    /*
        进行状态扭转
     */
    public Boolean processConvertStatus(ConvertActivityStatusDTO convertActivityStatusDTO,
                                        Map<String,AbstructActivityOperator> currMap,
                                        int sequence) {
        Boolean update = false;
        // 遍历currMap
        Iterator<Map.Entry<String, AbstructActivityOperator>> iterator = currMap.entrySet().iterator();
        while(iterator.hasNext()) {
            AbstructActivityOperator operator = iterator.next().getValue();
            // Operator是否需要转换
            if (operator.sequence() != sequence || !operator.needConvert(convertActivityStatusDTO)) {
                continue;
            }
            update = true;

            // 需要转换
            Boolean flag = operator.convert(convertActivityStatusDTO);
            if (!flag) {
                log.warn("convert activity status failed");
                throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_STATUS_CONVERT_ERROR);
            }

            // currMap删除当前遍历的Operator
            iterator.remove();
        }

        return update;
    }

}
