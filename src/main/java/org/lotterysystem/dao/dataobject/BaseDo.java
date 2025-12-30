package org.lotterysystem.dao.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/*
    基础模型
 */

@Data
public class BaseDo implements Serializable {
    /*
        主键
     */
    private Long id;

    /*
        创建时间
     */
    private Date gmtCreate;

    /*
        修改时间
     */
    private Date gmtModified;
}
