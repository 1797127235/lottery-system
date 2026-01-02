package org.lotterysystem.controller.result;

import lombok.Data;
import org.lotterysystem.service.dto.ActivityDetailDTO;
import org.lotterysystem.service.enums.ActivityPrizeStatusEnum;
import org.lotterysystem.service.enums.ActivityPrizeTiersEnum;
import org.lotterysystem.service.enums.ActivityStatusEnum;
import org.lotterysystem.service.enums.ActivityUserStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class GetActivityDetailResult implements Serializable {
    // 活动信息
    private  Long activityId;

    private String name;

    private String description;

    // 活动是否有效
    private Boolean valid;

    // 奖品信息
    private List<Prize> prizeDTOList;

    // 人员信息
    private List<User> userDTOList;



    @Data
    public static class Prize {
        /*
           奖品id
        */
        private Long prizeId;

        /*
        奖品名称
        */
        private String name;

        /*
            图片索引
         */
        private String imageUrl;

        /*
            价格
         */
        private BigDecimal price;

        /*
            描述
         */
        private String description;
        /*
            奖品等级
         */
        private String prizeTiersEnum;
        /*
            奖品数量
         */
        private Long prizeAmount;
        /*
            奖品是否有效
         */
        private Boolean valid;

    }

    @Data
    public static class User {
        /*
            用户id
         */
        private Long userId;

        /*
            姓名
         */
        private String userName;

        /*

         */

        public Boolean valid;

    }
}
