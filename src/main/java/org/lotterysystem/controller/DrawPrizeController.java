package org.lotterysystem.controller;


import lombok.extern.slf4j.Slf4j;
import org.lotterysystem.common.pojo.CommonResult;
import org.lotterysystem.controller.param.DrawPrizeParam;
import org.lotterysystem.service.DrawPrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DrawPrizeController {
    @Autowired
    private DrawPrizeService drawPrizeService;
    /*
        抽奖方法
     */
    @RequestMapping("/draw-prize")
    public CommonResult<Boolean> drawPrize(@Validated @RequestBody DrawPrizeParam param){
        log.info("drawPrize DrawPrizeParam: {}", param);
        drawPrizeService.drawPrize(param);

        return CommonResult.success(true);

    }
}
