package com.zephyr.mpbsfiles.service.Impl;

import com.zephyr.mpbsfiles.dto.FilesProcessDTO;
import com.zephyr.mpbsfiles.mapper.FileMapper;
import com.zephyr.mpbsfiles.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileMapper fileMapper;

    @Value("${file.upload-path}")
    private String uploadPath;

    @Override
    public List<FilesProcessDTO> getFilesListByRole(String userId) {
        return fileMapper.getAccessibleFilesByRole(Integer.parseInt(userId));
    }

    @Override
    public FilesProcessDTO uploadFile(MultipartFile file, String uploaderId) throws IOException {
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
        File dest = new File(uploadPath, storageFileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        file.transferTo(dest);

        // 构建 DTO
        FilesProcessDTO dto = new FilesProcessDTO();
        dto.setId(id);
        dto.setFilename(originalFilename);
        dto.setStoragePath(dest.getAbsolutePath());
        dto.setUploaderId(uploaderId);
        dto.setSize(String.valueOf(file.getSize()));
        dto.setMimeType(file.getContentType());

        // 存数据库
        fileMapper.insertFile(dto);

        return dto;
    }

    @Override
    public FilesProcessDTO getFileById(String id) {
        return fileMapper.selectFileById(id);
    }
}
