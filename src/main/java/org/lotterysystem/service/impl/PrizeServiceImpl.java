package org.lotterysystem.service.impl;

import org.lotterysystem.controller.param.CreatePrizeParam;
import org.lotterysystem.controller.param.PageParam;
import org.lotterysystem.dao.dataobject.PrizeDO;
import org.lotterysystem.dao.mapper.PrizeMapper;
import org.lotterysystem.service.PictureService;
import org.lotterysystem.service.PrizeService;
import org.lotterysystem.service.dto.PageListDTO;
import org.lotterysystem.service.dto.PrizeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrizeServiceImpl implements PrizeService {

    @Autowired
    private PictureService pictureService;

    @Autowired
    private PrizeMapper prizeMapper;

    @Override
    public Long createPrize(CreatePrizeParam param, MultipartFile multipartFile) {
        // 保存图片
        String imageUrl = pictureService.savePicture(multipartFile);

        // 落库
        PrizeDO prizeDO = new PrizeDO();
        prizeDO.setName(param.getPrizeName());
        prizeDO.setDescription(param.getDescription());
        prizeDO.setPrice(param.getPrice());
        prizeDO.setImageUrl(imageUrl);

        prizeMapper.insert(prizeDO);
        return prizeDO.getId();
    }

    @Override
    public PageListDTO<PrizeDTO> findPrizeList(PageParam param) {
        // 总量
        int total = prizeMapper.count();

        // 查询当前页列表
        List<PrizeDTO> prizeDTOlist = new ArrayList<>();
        List<PrizeDO> prizeDOlist = prizeMapper.selectPrizeList(param.offset(),param.getPageSize());

        for(PrizeDO prizeDO: prizeDOlist){
            PrizeDTO prizeDTO = new PrizeDTO();
            prizeDTO.setPrizeId(prizeDO.getId());
            prizeDTO.setName(prizeDO.getName());
            prizeDTO.setDescription(prizeDO.getDescription());
            prizeDTO.setPrice(prizeDO.getPrice());
            prizeDTO.setImageUrl(prizeDO.getImageUrl());
            prizeDTOlist.add(prizeDTO);
        }

        return new PageListDTO<>(total,prizeDTOlist);


    }


}
