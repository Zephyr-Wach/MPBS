package com.zephyr.mpbsfiles.service.Impl;

import com.zephyr.mpbsfiles.dto.MediaProcessDTO;
import com.zephyr.mpbsfiles.mapper.MediaMapper;
import com.zephyr.mpbsfiles.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class MediaServiceImpl implements MediaService {
    @Autowired
    private MediaMapper mediaMapper;

    @Value("${file.upload-path}")
    private String mediaPath;
//    private String mediaPath = systemUploadPath + "/Media";

    @Override
    public MediaProcessDTO uploadMedia(MultipartFile file, String uploaderId) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件为空");
        }

        // 生成唯一文件ID和文件名存储路径
        String id = UUID.randomUUID().toString().replace("-", "");
        String originalFilename = file.getOriginalFilename();
        String suffix = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }

        String storageFileName = id + suffix;
        File dest = new File(mediaPath, storageFileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        file.transferTo(dest);

        // 构建 DTO
        MediaProcessDTO dto = new MediaProcessDTO();
        dto.setId(id);
        dto.setFilename(originalFilename);
        dto.setStoragePath(dest.getAbsolutePath());
        dto.setUploaderId(uploaderId);
        dto.setSize(String.valueOf(file.getSize()));
        dto.setMimeType(file.getContentType());

        // 存数据库
        mediaMapper.insertMedia(dto);

        return dto;
    }

    @Override
    public MediaProcessDTO getMediaById(String mediaId) {
        return mediaMapper.getMediaById(mediaId);
    }

}
