package org.lotterysystem.service.impl;

import org.lotterysystem.common.utils.JacksonUtil;
import org.lotterysystem.controller.param.DrawPrizeParam;
import org.lotterysystem.service.DrawPrizeService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.lotterysystem.common.config.DirectRabbitConfig.EXCHANGE_NAME;
import static org.lotterysystem.common.config.DirectRabbitConfig.ROUTING;

@Service
public class DrawPrizeServiceImpl implements DrawPrizeService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void drawPrize(DrawPrizeParam param) {
        Map<String,String> map = new HashMap<>();
        map.put("messageId",String.valueOf(UUID.randomUUID()));
        map.put("messageData", JacksonUtil.writeValueAsString(param));

        // 发消息: 交换机，绑定的key，消息体
        rabbitTemplate.convertAndSend(EXCHANGE_NAME,ROUTING,map);
        // 返回
    }
}
