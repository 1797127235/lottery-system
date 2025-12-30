package org.lotterysystem.service;

import org.springframework.web.multipart.MultipartFile;

public interface PictureService {
    /*
        保存图片的方法
        @return 返回索引 文件名(唯一)
     */
    String savePicture(MultipartFile multipartFile);
}
