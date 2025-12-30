package org.lotterysystem.service;

import org.lotterysystem.controller.param.CreatePrizeParam;
import org.lotterysystem.controller.param.PageParam;
import org.lotterysystem.service.dto.PageListDTO;
import org.lotterysystem.service.dto.PrizeDTO;
import org.springframework.web.multipart.MultipartFile;

public interface PrizeService {
    /*

     */
    Long createPrize(CreatePrizeParam param, MultipartFile multipartFile);

    /*
        翻页查询列表
     */
    PageListDTO<PrizeDTO> findPrizeList(PageParam param);
}
