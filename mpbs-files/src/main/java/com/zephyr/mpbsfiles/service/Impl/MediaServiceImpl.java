package com.zephyr.mpbsfiles.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zephyr.mpbsfiles.dto.MediaProcessDTO;
import com.zephyr.mpbsfiles.entity.MediaEntity;
import com.zephyr.mpbsfiles.mapper.MediaMapper;
import com.zephyr.mpbsfiles.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

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

    @Override
    public Map<String, Object> listMediaByUser(String userId, int page, int size) {
        Page<MediaEntity> mediaPage = new Page<>(page, size);
        QueryWrapper<MediaEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("uploader_id", userId).orderByDesc("created_at");

        mediaMapper.selectPage(mediaPage, wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("total", mediaPage.getTotal());
        result.put("pages", mediaPage.getPages());
        result.put("current", mediaPage.getCurrent());
        result.put("size", mediaPage.getSize());
        result.put("records", mediaPage.getRecords());
        return result;
    }

    @Override
    public boolean deleteMedia(String mediaId, String userId) {
        MediaEntity entity = mediaMapper.selectById(mediaId);
        if (entity == null || !userId.equals(entity.getUploaderId())) {
            return false;
        }
        return mediaMapper.deleteById(mediaId) > 0;
    }

}
