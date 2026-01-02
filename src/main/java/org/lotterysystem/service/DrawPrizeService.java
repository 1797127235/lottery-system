package org.lotterysystem.service;

import org.lotterysystem.controller.param.DrawPrizeParam;

public interface DrawPrizeService {
    /*
        异步抽奖接口
     */
    void drawPrize(DrawPrizeParam param);

}
