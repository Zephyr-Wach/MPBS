package com.zephyr.mpbsfiles.service.Impl;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsfiles.dto.FilePermissionDTO;
import com.zephyr.mpbsfiles.dto.FilesProcessDTO;
import com.zephyr.mpbsfiles.dto.ShareLink;
import com.zephyr.mpbsfiles.entity.FilesEntity;
import com.zephyr.mpbsfiles.mapper.FileMapper;
import com.zephyr.mpbsfiles.mapper.FilePermissionMapper;
import com.zephyr.mpbsfiles.mapper.ShareMapper;
import com.zephyr.mpbsfiles.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private ShareMapper shareMapper;

    @Autowired
    private FilePermissionMapper permissionMapper;

    @Value("${file.upload-path}")
    private String uploadPath;

    @Value("${public.base-url}")
    private String baseUrl;

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

    @Override
    public Result<String> shareFile(String fileId) {
        // 1. 查询文件是否存在
        FilesProcessDTO file = fileMapper.selectFileById(fileId);
        if (file == null) {
            return Result.failure(400,"文件不存在");
        }

        // 2. 生成唯一的分享ID和链接
        String shareId = UUID.randomUUID().toString().replace("-", "");
        String url = baseUrl + "/api/files/share/download/" + shareId;

        // 3. 构造分享记录
        ShareLink share = new ShareLink();
        share.setId(shareId);
        share.setFileId(fileId);
        share.setExpiresAt(String.valueOf(LocalDateTime.now().plusDays(2))); // 默认有效期2天
        share.setCreatedAt(String.valueOf(LocalDateTime.now()));
        share.setUrl(url);

        // 4. 保存到数据库
        shareMapper.insert(share);

        // 5. 返回分享链接
        return Result.success(url);
    }

    @Override
    public Result deleteFile(String fileId) {
        FilesProcessDTO file = fileMapper.selectFileById(fileId);
        if (file != null) {
            // 删除文件
            File storageFile = new File(file.getStoragePath());
            if (storageFile.exists()) {
                storageFile.delete();
            }

            // 删除记录
            fileMapper.deleteById(fileId);
            return fileMapper.selectFileById(fileId) != null?
                    Result.failure(400, "文件删除失败"):
                    Result.success("文件删除成功");
        }
        return Result.failure(400, "文件不存在");
    }
}
