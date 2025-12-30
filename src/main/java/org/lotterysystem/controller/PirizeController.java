package org.lotterysystem.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.lotterysystem.common.errcode.ControllerErrorCodeConstants;
import org.lotterysystem.common.exception.ControllerException;
import org.lotterysystem.common.pojo.CommonResult;
import org.lotterysystem.controller.param.CreatePrizeParam;
import org.lotterysystem.controller.param.PageParam;
import org.lotterysystem.controller.result.FindPrizeListResult;
import org.lotterysystem.service.PictureService;
import org.lotterysystem.service.PrizeService;
import org.lotterysystem.service.dto.PageListDTO;
import org.lotterysystem.service.dto.PrizeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class PirizeController {

    @Autowired
    private PictureService pictureService;

    @Autowired
    private PrizeService prizeService;

    @PostMapping("/picture/upload")
    public String uploadPicture(MultipartFile multipartFile) {
        String uploadPic = pictureService.savePicture(multipartFile);

        return uploadPic;
    }

    /*
        接收表单数据
     */
    @PostMapping(value = "/prize/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult<Long> createPrize(
            @RequestParam("param") String paramJson,
            @RequestPart("prizePic") MultipartFile multipartFile) throws JsonProcessingException {
        log.info("createPrize paramJson={}, prizePic={}", paramJson, multipartFile);
        CreatePrizeParam param = new ObjectMapper().readValue(paramJson, CreatePrizeParam.class);
        Long res = prizeService.createPrize(param, multipartFile);
        return CommonResult.success(res);
    }

    @RequestMapping("/prize/find-list")
    public CommonResult<FindPrizeListResult> findPrizeList(PageParam param) {
        log.info("findPrizeList param={}", param);
        PageListDTO<PrizeDTO> prizeList = prizeService.findPrizeList(param);

        return CommonResult.success(convertToFindPrizeListResult(prizeList));
    }

    private FindPrizeListResult convertToFindPrizeListResult(PageListDTO<PrizeDTO> prizeList) {
        if (null == prizeList) {
            throw new ControllerException(ControllerErrorCodeConstants.FIND_PRIZE_LIST_ERROR);
        }

        FindPrizeListResult result = new FindPrizeListResult();

        result.setTotal(prizeList.getTotal());
        result.setRecords(prizeList.getRecords()
                .stream()
                .map(dto -> {
                    FindPrizeListResult.PrizeInfo info = new FindPrizeListResult.PrizeInfo();
                    info.setPrizeId(dto.getPrizeId());
                    info.setPrizeName(dto.getName());
                    info.setDescription(dto.getDescription());
                    info.setPrice(dto.getPrice());
                    info.setImageUrl(dto.getImageUrl());
                    return info;
                })
                .toList());

        return result;

    }
}
