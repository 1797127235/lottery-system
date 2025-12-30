package org.lotterysystem.service.impl;

import org.lotterysystem.common.errcode.ServiceErrorCodeConstants;
import org.lotterysystem.common.exception.ServiceException;
import org.lotterysystem.controller.param.CreateActivityParam;
import org.lotterysystem.dao.dataobject.ActivityDO;
import org.lotterysystem.dao.dataobject.ActivityPrizeDO;
import org.lotterysystem.dao.dataobject.ActivityUserDO;
import org.lotterysystem.dao.mapper.ActivityMapper;
import org.lotterysystem.dao.mapper.ActivityPrizeMapper;
import org.lotterysystem.dao.mapper.ActivityUserMapper;
import org.lotterysystem.service.ActivityService;
import org.lotterysystem.service.dto.CreateActivityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;

    @Autowired
    private ActivityUserMapper activityUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CreateActivityDTO createActivity(CreateActivityParam param) {
        // 基础校验：列表非空已由 @Validated 处理，这里补充业务保护
        if (CollectionUtils.isEmpty(param.getActivityPrizeList())) {
            throw new IllegalArgumentException("活动奖品不能为空");
        }
        if (CollectionUtils.isEmpty(param.getActivityUserList())) {
            throw new IllegalArgumentException("活动关联人员不能为空");
        }

        // 构建DO落库
        ActivityDO activityDO = new ActivityDO();
        activityDO.setActivityName(param.getActivityName());
        activityDO.setDescription(param.getDescription());
        activityDO.setStatus("ENABLE");

        activityMapper.insert(activityDO);

        // 落库活动-奖品关联
        List<ActivityPrizeDO> activityPrizeDOList = new ArrayList<>();
        param.getActivityPrizeList().forEach(prizeParam -> {
            ActivityPrizeDO ap = new ActivityPrizeDO();
            ap.setActivityId(activityDO.getId());
            ap.setPrizeId(prizeParam.getPrizeId());
            ap.setPrizeAmount(prizeParam.getAnoMount());
            ap.setPrizeTiers(prizeParam.getPrizeTiers());
            ap.setStatus("ENABLE");
            activityPrizeDOList.add(ap);
        });
        activityPrizeDOList.forEach(activityPrizeMapper::insert);

        // 落库活动-人员关联
        List<ActivityUserDO> activityUserDOList = new ArrayList<>();
        param.getActivityUserList().forEach(userParam -> {
            ActivityUserDO au = new ActivityUserDO();
            au.setActivityId(activityDO.getId());
            au.setUserId(userParam.getUserId());
            au.setUserName(userParam.getUserName());
            au.setStatus("ENABLE");
            activityUserDOList.add(au);
        });
        activityUserDOList.forEach(activityUserMapper::insert);

        // TODO: 可补充写入缓存等操作

        return new CreateActivityDTO(activityDO.getId());
    }

    /*
        校验活动是否有效
     */
    private void checkActivityInfo(CreateActivityParam param) {
        if(null == param) {
            throw new ServiceException(ServiceErrorCodeConstants.)
        }
    }
}
