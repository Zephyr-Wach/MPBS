package com.zephyr.mpbsfiles.controller;


import com.zephyr.mpbscommon.annotation.LogOperation;
import com.zephyr.mpbsfiles.dto.FilesProcessDTO;
import com.zephyr.mpbsfiles.dto.ShareLink;
import com.zephyr.mpbsfiles.mapper.ShareMapper;
import com.zephyr.mpbsfiles.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/public/files")
public class PublicController {

    @Autowired
    private FileService fileService;

    @Autowired
    private ShareMapper shareMapper;

    /**
     * 通过分享链接下载文件
     * @param id 分享链接ID
     * @return 响应体，包含文件资源或错误状态码（404未找到，410已过期等）
     */
    @GetMapping("/share/download/{id}")
    @LogOperation(operationType = "通过分享链接下载文件")
    public ResponseEntity<Resource> downloadSharedFile(@PathVariable String id) {
        ShareLink share = shareMapper.getShareById(id);
        if (share == null) {
            return ResponseEntity.notFound().build();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime expiresAt = LocalDateTime.parse(share.getExpiresAt(), formatter);
        LocalDateTime now = LocalDateTime.now();
        if (expiresAt.isBefore(now)) {
            return ResponseEntity.status(410).build();
        }

        FilesProcessDTO dto = fileService.getFileById(share.getFileId());
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        File file = new File(dto.getStoragePath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            String encodedFilename = URLEncoder.encode(dto.getFilename(), StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType(dto.getMimeType()))
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
}
