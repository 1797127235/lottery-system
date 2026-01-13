package org.lotterysystem.service;

import org.lotterysystem.controller.param.DrawPrizeParam;

public interface DrawPrizeService {
    /*
        异步抽奖接口
     */
    void drawPrize(DrawPrizeParam param);

    /*
        校验抽奖请求
     */
    void checkDrawPrizeParam(DrawPrizeParam param);

    /*
        保存中奖者名单
     */
    void saveWinnerRecords(DrawPrizeParam param);

}
