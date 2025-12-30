package org.lotterysystem.dao.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class PrizeDO extends BaseDo {
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
}
