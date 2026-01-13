package org.lotterysystem.service.impl;

import org.lotterysystem.common.errcode.ServiceErrorCodeConstants;
import org.lotterysystem.common.exception.ServiceException;
import org.lotterysystem.common.utils.JacksonUtil;
import org.lotterysystem.controller.param.DrawPrizeParam;
import org.lotterysystem.dao.dataobject.ActivityDO;
import org.lotterysystem.dao.dataobject.ActivityPrizeDO;
import org.lotterysystem.dao.dataobject.PrizeDO;
import org.lotterysystem.dao.dataobject.UserDo;
import org.lotterysystem.dao.dataobject.WinnerRecordDO;
import org.lotterysystem.dao.mapper.ActivityMapper;
import org.lotterysystem.dao.mapper.ActivityPrizeMapper;
import org.lotterysystem.dao.mapper.PrizeMapper;
import org.lotterysystem.dao.mapper.UserMapper;
import org.lotterysystem.dao.mapper.WinnerRecordMapper;
import org.lotterysystem.common.utils.RedisUtil;
import org.lotterysystem.service.DrawPrizeService;
import org.lotterysystem.service.enums.ActivityStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.lotterysystem.common.config.DirectRabbitConfig.EXCHANGE_NAME;
import static org.lotterysystem.common.config.DirectRabbitConfig.ROUTING;

@Service
public class DrawPrizeServiceImpl implements DrawPrizeService {

    private static final Logger log = LoggerFactory.getLogger(DrawPrizeServiceImpl.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ActivityMapper  activityMapper;
    @Autowired
    private ActivityPrizeMapper  activityPrizeMapper;
    @Autowired
    private WinnerRecordMapper winnerRecordMapper;
    @Autowired
    private PrizeMapper prizeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void drawPrize(DrawPrizeParam param) {
        Map<String,String> map = new HashMap<>();
        map.put("messageId",String.valueOf(UUID.randomUUID()));
        map.put("messageData", JacksonUtil.writeValueAsString(param));

        // 发消息: 交换机，绑定的key，消息体
        rabbitTemplate.convertAndSend(EXCHANGE_NAME,ROUTING,map);
        // 返回
    }

    @Override
    public void checkDrawPrizeParam(DrawPrizeParam param) {
        if (param == null) {
            throw new ServiceException(ServiceErrorCodeConstants.CREATE_ACTIVITY_INFO_IS_EMPTY);
        }
        // 活动是否存在
        ActivityDO activityDO = activityMapper.selectById(param.getActivityId());
        ActivityPrizeDO activityPrizeDO = activityPrizeMapper.selectByAPId(
          param.getActivityId(),param.getPrizeId()
        );
        if(null == activityDO ||  null == activityPrizeDO) {
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_OR_PRIZE_IS_EMPTY);
        }

        // 活动是否有效
        if(activityDO.getStatus().equalsIgnoreCase(ActivityStatusEnum.COMPLETED.name())) {
            throw new  ServiceException(ServiceErrorCodeConstants.ACTIVITY_COMPLETED);
        }

        // 奖品是否有效
        if(activityPrizeDO.getStatus().equalsIgnoreCase(ActivityStatusEnum.COMPLETED.name())) {
            throw new  ServiceException(ServiceErrorCodeConstants.PRIZE_COMPLETED);
        }

        // 中奖者人数是否和设置的奖品一致
        if(activityPrizeDO.getPrizeAmount() != param.getWinnerList().size()) {
            throw  new ServiceException(ServiceErrorCodeConstants.WINNER_PRIZE_AMOUNT_ERROR);
        }



    }

    @Override
    public void saveWinnerRecords(DrawPrizeParam param) {
        if (param.getWinnerList() == null || param.getWinnerList().isEmpty()) {
            return;
        }

        // 查询活动信息
        ActivityDO activityDO = activityMapper.selectById(param.getActivityId());
        // 查询奖品信息
        List<PrizeDO> prizeList = prizeMapper.selectByIds(List.of(param.getPrizeId()));
        String prizeName = prizeList.isEmpty() ? null : prizeList.get(0).getName();

        // 查询中奖用户信息
        List<Long> winnerIds = param.getWinnerList().stream()
                .map(DrawPrizeParam.Winner::getUserId)
                .toList();
        Map<Long, UserDo> userMap = new HashMap<>();
        for (Long winnerId : winnerIds) {
            UserDo user = userMapper.selectById(winnerId);
            if (user != null) {
                userMap.put(winnerId, user);
            }
        }
        /*
            一个活动的某种奖品
            构造中奖结果列表
         */
        List<WinnerRecordDO> winnerRecordList = new ArrayList<>();
        for (DrawPrizeParam.Winner winner : param.getWinnerList()) {
            WinnerRecordDO winnerRecordDO = new WinnerRecordDO();
            winnerRecordDO.setActivityId(param.getActivityId());
            winnerRecordDO.setActivityName(activityDO != null ? activityDO.getActivityName() : null);
            winnerRecordDO.setPrizeId(param.getPrizeId());
            winnerRecordDO.setPrizeName(prizeName);
            winnerRecordDO.setPrizeTier(param.getPrizeTiers());
            winnerRecordDO.setWinnerId(winner.getUserId());
            winnerRecordDO.setWinnerName(winner.getUserName());

            UserDo user = userMap.get(winner.getUserId());
            if (user != null) {
                winnerRecordDO.setWinnerEmail(user.getEmail());
                winnerRecordDO.setWinnerPhoneNumber(user.getPhoneNumber() != null ? user.getPhoneNumber().getValue() : null);
            }

            winnerRecordDO.setWinningTime(param.getWinningTime());
            winnerRecordList.add(winnerRecordDO);
        }

        winnerRecordMapper.batchInsert(winnerRecordList);

        // 缓存中奖者信息到 Redis
        cacheWinnerRecords(param.getActivityId(), param.getPrizeId(), winnerRecordList);
    }

    private void cacheWinnerRecords(Long activityId, Long prizeId, List<WinnerRecordDO> winnerRecordList) {
        try {
            String cacheKey = "winner:activity:" + activityId + ":prize:" + prizeId;
            String cacheValue = JacksonUtil.writeValueAsString(winnerRecordList);
            // 缓存30天
            redisUtil.set(cacheKey, cacheValue, 30 * 24 * 60 * 60L);
        } catch (Exception e) {
            log.error("缓存中奖者信息到Redis失败, activityId={}, prizeId={}", activityId, prizeId, e);
        }
    }
}
