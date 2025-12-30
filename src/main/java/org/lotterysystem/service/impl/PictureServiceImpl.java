package org.lotterysystem.service.impl;

import org.lotterysystem.common.errcode.ServiceErrorCodeConstants;
import org.lotterysystem.common.exception.ServiceException;
import org.lotterysystem.service.PictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class PictureServiceImpl implements PictureService {

    private static final Logger log = LoggerFactory.getLogger(PictureServiceImpl.class);

    // 默认使用运行目录下的 upload 文件夹，可在配置中覆盖 file.upload-dir
    @Value("${file.upload-dir:${user.dir}/upload}")
    private String uploadDirPath;

    @Override
    public String savePicture(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("上传文件为空");
        }

        // 生成唯一文件名，保留原始扩展名
        String original = multipartFile.getOriginalFilename();
        String ext = "";  // 后缀
        if (original != null && original.lastIndexOf(".") >= 0) {
            ext = original.substring(original.lastIndexOf("."));
        }
        String filename = UUID.randomUUID().toString().replace("-", "") + ext;

        // 保存路径：来自配置 file.upload-dir，默认 src/main/resources/static/upload
        Path uploadDir = Paths.get(uploadDirPath);
        try {
            Files.createDirectories(uploadDir);
            Path target = uploadDir.resolve(filename);
            multipartFile.transferTo(target.toFile());
        } catch (IOException e) {
            log.error("保存图片失败, path={}, filename={}, error={}", uploadDirPath, filename, e.getMessage(), e);
            throw new ServiceException(ServiceErrorCodeConstants.PIC_UPLOAD_ERROR);
        }

        return filename;
    }
}
