package org.lotterysystem.service.activitystatus.operator;

import org.lotterysystem.service.dto.ConvertActivityStatusDTO;

public abstract class AbstructActivityOperator {
    /*
        处理流程的序号
     */
    public abstract Integer sequence();

    /*
        是否需要转换
     */
    public abstract Boolean needConvert(ConvertActivityStatusDTO convertActivityStatusDTO);

    /*
        对状态进行扭转
     */
    public abstract Boolean convert(ConvertActivityStatusDTO convertActivityStatusDTO);
}
