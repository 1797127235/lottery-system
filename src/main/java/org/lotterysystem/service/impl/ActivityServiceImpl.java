package org.lotterysystem.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lotterysystem.common.errcode.ServiceErrorCodeConstants;
import org.lotterysystem.common.exception.ServiceException;
import org.lotterysystem.common.utils.JacksonUtil;
import org.lotterysystem.common.utils.RedisUtil;
import org.lotterysystem.controller.param.CreateActivityParam;
import org.lotterysystem.controller.param.CreatePrizeByActivityParam;
import org.lotterysystem.controller.param.CreateUserByActivityParam;
import org.lotterysystem.controller.param.PageParam;
import org.lotterysystem.dao.dataobject.ActivityDO;
import org.lotterysystem.dao.dataobject.ActivityPrizeDO;
import org.lotterysystem.dao.dataobject.ActivityUserDO;
import org.lotterysystem.dao.dataobject.PrizeDO;
import org.lotterysystem.dao.mapper.*;
import org.lotterysystem.service.ActivityService;
import org.lotterysystem.service.dto.ActivityDTO;
import org.lotterysystem.service.dto.ActivityDetailDTO;
import org.lotterysystem.service.dto.CreateActivityDTO;
import org.lotterysystem.service.dto.PageListDTO;
import org.lotterysystem.service.enums.ActivityPrizeStatusEnum;
import org.lotterysystem.service.enums.ActivityPrizeTiersEnum;
import org.lotterysystem.service.enums.ActivityStatusEnum;
import org.lotterysystem.service.enums.ActivityUserStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ActivityServiceImpl implements ActivityService {

    private static final Logger log = LoggerFactory.getLogger(ActivityServiceImpl.class);

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;

    @Autowired
    private ActivityUserMapper activityUserMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PrizeMapper prizeMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ObjectMapper objectMapper;

    /*
        创建活动
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CreateActivityDTO createActivity(CreateActivityParam param) {
        //
        checkActivityInfo(param);

        // 构建DO落库
        ActivityDO activityDO = new ActivityDO();
        activityDO.setActivityName(param.getActivityName());
        activityDO.setDescription(param.getDescription());
        activityDO.setStatus(ActivityStatusEnum.RUNNING.name());

        activityMapper.insert(activityDO);

        // 落库活动-奖品关联
        List<ActivityPrizeDO> activityPrizeDOList = new ArrayList<>();
        param.getActivityPrizeList().forEach(prizeParam -> {
            ActivityPrizeDO ap = new ActivityPrizeDO();
            ap.setActivityId(activityDO.getId());
            ap.setPrizeId(prizeParam.getPrizeId());
            ap.setPrizeAmount(prizeParam.getAnoMount());
            ap.setPrizeTiers(prizeParam.getPrizeTiers());
            ap.setStatus(ActivityPrizeStatusEnum.INIT.name());
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
            au.setStatus(ActivityUserStatusEnum.INIT.name());
            activityUserDOList.add(au);
        });
        activityUserDOList.forEach(activityUserMapper::insert);

        // 写入缓存
        ActivityDetailDTO activityDetailDTO = convertToActivityDetailDTO(
                activityDO,
                activityPrizeDOList,
                activityUserDOList
        );

        // 缓存活动详情到 redis
        cacheActivity(activityDetailDTO);

        return new CreateActivityDTO(activityDO.getId());
    }
    /*
        查询当前页活动列表
     */
    @Override
    public PageListDTO<ActivityDTO> findActivityList(PageParam pageParam) {
        // 获取总量
        int total = activityMapper.count();

        // 获取当前页列表
        List<ActivityDO> activityDOList = activityMapper.selectActivityList(pageParam.offset(),pageParam.getPageSize());

        List<ActivityDTO> activityDTOList = activityDOList.stream()
                .map(activityDO -> {
                    ActivityDTO activityDTO = new ActivityDTO();
                    activityDTO.setActivityId(activityDO.getId());
                    activityDTO.setActivityName(activityDO.getActivityName());
                    activityDTO.setDescription(activityDO.getDescription());
                    activityDTO.setStatus(ActivityStatusEnum.forName(activityDO.getStatus()));
                    return activityDTO;
                }).collect(Collectors.toList());

        return new PageListDTO<>(total, activityDTOList);
    }
    /*
        查活动详情
     */
    @Override
    public ActivityDetailDTO getActivityDetail(Long activityId) {
        if(null == activityId){
            log.error("activityId is null");
            return null;
        }
        // 查redis
        ActivityDetailDTO activityDetailDTO = getActivityDetailDTO(activityId);
        if (activityDetailDTO != null) {
            return activityDetailDTO;
        }

        // redis不存在，查表
        ActivityDO activityDO = activityMapper.selectById(activityId);
        if (activityDO == null) {
            log.warn("activity not found, id={}", activityId);
            return null;
        }

        List<ActivityPrizeDO> activityPrizeDOList = activityPrizeMapper.selectByActivityId(activityId);
        List<ActivityUserDO> activityUserDOList = activityUserMapper.selectByActivityId(activityId);

        ActivityDetailDTO detailDTO = convertToActivityDetailDTO(activityDO, activityPrizeDOList, activityUserDOList);

        // 缓存
        cacheActivity(detailDTO);

        return detailDTO;
    }

    @Override
    public void cacheActivity(Long activityId) {
        if(null == activityId){
            log.error("activityId is null");
            throw new ServiceException(ServiceErrorCodeConstants.CACHE_ACTIVITY_ID_IS_EMPTY);
        }

        ActivityDO activityDO = activityMapper.selectById(activityId);
        if (activityDO == null) {
            log.warn("activity not found, id={}", activityId);
            return;
        }

        List<ActivityPrizeDO> activityPrizeDOList = activityPrizeMapper.selectByActivityId(activityId);
        List<ActivityUserDO> activityUserDOList = activityUserMapper.selectByActivityId(activityId);

        ActivityDetailDTO detailDTO = convertToActivityDetailDTO(activityDO, activityPrizeDOList, activityUserDOList);
        cacheActivity(detailDTO);
    }

    private ActivityDetailDTO convertToActivityDetailDTO(ActivityDO activityDO,
                                                         List<ActivityPrizeDO> activityPrizeDOList,
                                                         List<ActivityUserDO> activityUserDOList) {
        ActivityDetailDTO activityDetailDTO = new ActivityDetailDTO();
        // 基础信息
        activityDetailDTO.setActivityId(activityDO.getId());
        activityDetailDTO.setName(activityDO.getActivityName());
        activityDetailDTO.setDescription(activityDO.getDescription());
        activityDetailDTO.setStatus(ActivityStatusEnum.valueOf(activityDO.getStatus()));

        // 奖品信息封装
        List<Long> prizeIds = activityPrizeDOList.stream()
                .map(ActivityPrizeDO::getPrizeId)
                .distinct()
                .collect(Collectors.toList());
        List<PrizeDO> prizeDOList = CollectionUtils.isEmpty(prizeIds) ? new ArrayList<>() : prizeMapper.selectByIds(prizeIds);
        Map<Long, PrizeDO> prizeMap = prizeDOList.stream().collect(Collectors.toMap(PrizeDO::getId, p -> p));

        List<ActivityDetailDTO.PrizeDTO> prizeDTOS = activityPrizeDOList.stream().map(ap -> {
            ActivityDetailDTO.PrizeDTO dto = new ActivityDetailDTO.PrizeDTO();
            dto.setPrizeId(ap.getPrizeId());
            PrizeDO prizeDO = prizeMap.get(ap.getPrizeId());
            if (prizeDO != null) {
                dto.setName(prizeDO.getName());
                dto.setImageUrl(prizeDO.getImageUrl());
                dto.setPrice(prizeDO.getPrice());
                dto.setDescription(prizeDO.getDescription());
            }
            dto.setPrizeTiersEnum(ActivityPrizeTiersEnum.forName(ap.getPrizeTiers()));
            dto.setPrizeAmount(ap.getPrizeAmount());
            dto.setStatus(ActivityPrizeStatusEnum.valueOf(ap.getStatus()));
            return dto;
        }).collect(Collectors.toList());
        activityDetailDTO.setPrizeDTOList(prizeDTOS);

        // 人员信息封装
        List<ActivityDetailDTO.UserDTO> userDTOS = activityUserDOList.stream().map(au -> {
            ActivityDetailDTO.UserDTO dto = new ActivityDetailDTO.UserDTO();
            dto.setUserId(au.getUserId());
            dto.setUserName(au.getUserName());
            dto.setStatus(ActivityUserStatusEnum.valueOf(au.getStatus()));
            return dto;
        }).collect(Collectors.toList());
        activityDetailDTO.setUserDTOList(userDTOS);

        return activityDetailDTO;
    }

    /*
        校验活动是否有效
     */
    private void checkActivityInfo(CreateActivityParam param) {
        if(null == param) {
            throw new ServiceException(ServiceErrorCodeConstants.CREATE_ACTIVITY_INFO_IS_EMPTY);
        }

        // 人员id是否存在
        List<Long> userids = param.getActivityUserList()
                .stream()
                .map(CreateUserByActivityParam::getUserId)
                .distinct()
                .collect(Collectors.toList());

        List<Long> existUserIds = userMapper.selectExistByids(userids);

        userids.forEach(id -> {
            if(!existUserIds.contains(id)) {
                throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_USER_ERROR);
            }
        });

        //奖品id是否存在
        List<Long>  prizeids = param.getActivityPrizeList()
                .stream()
                .map(CreatePrizeByActivityParam::getPrizeId)
                .collect(Collectors.toList());
        List<Long> existPrizeIds = prizeMapper.selectExistByids(prizeids);

        prizeids.forEach(id -> {
            if(!existPrizeIds.contains(id)) {
                throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_PRIZE_ERROR);
            }
        });

        // 人员数量大于奖品数量
        Integer userAmount =  param.getActivityUserList().size();
        Long prizeAmount =  param.getActivityPrizeList()
                .stream()
                .mapToLong(CreatePrizeByActivityParam::getAnoMount)
                .sum();

        if(userAmount < prizeAmount) {
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_PRIZE_ERROR);
        }

        // 校验奖品等级的有效性

        param.getActivityPrizeList().forEach(prize -> {
            if(null == ActivityPrizeTiersEnum.forName(prize.getPrizeTiers())) {
                throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_PRIZE_TIERS_ERROR);
            }
        });

    }

    private void cacheActivity(ActivityDetailDTO detailDTO) {
        if(null == detailDTO || null == detailDTO.getActivityId()) {
            log.warn("要缓存的活动信息不存在");
            return;
        }
        try {
            String cacheKey = "activity:" + detailDTO.getActivityId();
            String cacheValue = objectMapper.writeValueAsString(detailDTO);
            redisUtil.set(cacheKey, cacheValue, 30, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            log.error("缓存活动信息到redis失败, activityId={}", detailDTO.getActivityId(), e);
        }
    }

    /*
        根据活动id
        从缓存中获取活动
     */
    private ActivityDetailDTO getActivityDetailDTO(Long id) {
        if(null == id) {
            log.warn("获取缓存活动数据的id为空");
            return null;
        }
        String s = null;
        try {
            s = redisUtil.get("activity:" + id);
        } catch (Exception e) {
            log.error("从缓存中获取活动信息异常，activityId={}", id, e);
            return null;
        }
        if(null == s) {
            return null;
        }

        ActivityDetailDTO activityDetailDTO = JacksonUtil.readValue(s, ActivityDetailDTO.class);

        return activityDetailDTO;


    }

}
