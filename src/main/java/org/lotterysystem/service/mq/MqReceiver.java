package org.lotterysystem.service.mq;

import lombok.extern.slf4j.Slf4j;
import org.lotterysystem.common.exception.ServiceException;
import org.lotterysystem.common.utils.JacksonUtil;
import org.lotterysystem.controller.param.DrawPrizeParam;
import org.lotterysystem.service.DrawPrizeService;
import org.lotterysystem.service.activitystatus.ActivityStatusManager;
import org.lotterysystem.service.dto.ConvertActivityStatusDTO;
import org.lotterysystem.service.enums.ActivityPrizeStatusEnum;
import org.lotterysystem.service.enums.ActivityUserStatusEnum;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.lotterysystem.common.config.DirectRabbitConfig.QUEUE_NAME;

@Slf4j
@Component
@RabbitListener(queues = QUEUE_NAME)
public class MqReceiver {
    @Autowired
    private DrawPrizeService drawPrizeService;

    @Autowired
    private ActivityStatusManager activityStatusManager;

    @RabbitHandler
    public void process(Map<String,String> message) {
        log.info("MQ成功接收到消息,message:{}", JacksonUtil.writeValueAsString(message));

        DrawPrizeParam param =  JacksonUtil.readValue(message.get("messageData"), DrawPrizeParam.class);
        /*
            异常需要保证事务一致性
         */
        try {
            // 校验
            drawPrizeService.checkDrawPrizeParam(param);

            // 活动/奖品/人员 状态扭转
            statusConvert(param);
            // 保存中奖者名单
            drawPrizeService.saveWinnerRecords(param);
            // 通知中奖者

        }  catch (ServiceException e) {
            log.error("处理MQ消息异常:{}:{}",e.getMessage(),e.getCode());
        } catch (Exception e) {
            log.error("处理MQ消息异常",e);
        }
    }

    private void statusConvert(DrawPrizeParam param) {
        ConvertActivityStatusDTO convertActivityStatusDTO = new ConvertActivityStatusDTO();
        convertActivityStatusDTO.setActivityId(param.getActivityId());
        convertActivityStatusDTO.setPrizeId(param.getPrizeId());
        // 本次开奖的奖品/用户状态扭转为已完成
        convertActivityStatusDTO.setTargetPrizeStatus(ActivityPrizeStatusEnum.COMPLETED);
        convertActivityStatusDTO.setTargetUserStatus(ActivityUserStatusEnum.COMPLETED);
        // 中奖用户列表
        if (param.getWinnerList() != null) {
            convertActivityStatusDTO.setUserIds(
                    param.getWinnerList()
                            .stream()
                            .map(DrawPrizeParam.Winner::getUserId)
                            .toList()
            );
        }

        activityStatusManager.handlerEvent(convertActivityStatusDTO);
    }

}
