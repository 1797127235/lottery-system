package org.lotterysystem;
import org.junit.jupiter.api.Test;
import org.lotterysystem.controller.param.DrawPrizeParam;
import org.lotterysystem.service.DrawPrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class DrawPrizeTest {

    @Autowired
    private DrawPrizeService drawPrizeService;

    @Test
    void drawPrize() {
        DrawPrizeParam param = new DrawPrizeParam();
        param.setActivityId(1L);
        param.setPrizeId(1L);
        param.setPrizeTiers("FIRST_PRIZE");
        param.setWinningTime(new Date());
        List<DrawPrizeParam.Winner> winners = new ArrayList<>();
        DrawPrizeParam.Winner winner = new DrawPrizeParam.Winner();
        winner.setUserId(42L);
        winner.setUserName("zhangsan");
        winners.add(winner);
        param.setWinnerList(winners);

        drawPrizeService.drawPrize(param);
    }
}
