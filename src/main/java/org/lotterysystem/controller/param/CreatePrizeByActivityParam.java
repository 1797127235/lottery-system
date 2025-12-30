package org.lotterysystem.controller.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreatePrizeByActivityParam implements Serializable {
    /*
        奖品id
     */
    @NotNull(message = "奖品id不能为空")
    @Positive(message = "奖品id必须为正数")
    private Long prizeId;

    /*
        奖品数量
     */
    @NotNull(message = "奖品数量不能为空")
    @Positive(message = "奖品数量必须为正数")
    private Long anoMount;

    /*
        奖品等级
     */
    @NotBlank(message = "奖品等级不能为空")
    private String prizeTiers;
}
