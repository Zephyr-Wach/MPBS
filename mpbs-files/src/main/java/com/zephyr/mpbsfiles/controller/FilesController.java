package com.zephyr.mpbsfiles.controller;

import com.zephyr.mpbscommon.utils.BeanConvertUtil;
import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsfiles.dto.FilesProcessDTO;
import com.zephyr.mpbsfiles.dto.ShareLink;
import com.zephyr.mpbsfiles.mapper.ShareMapper;
import com.zephyr.mpbsfiles.service.FileService;
import com.zephyr.mpbsfiles.vo.FilesProcessVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/files")
public class FilesController {

    @Autowired
    private FileService fileService;

    @Autowired
    private ShareMapper shareMapper;

    /**
     * 根据当前用户的token获取该用户角色可访问的文件列表
     * @param authentication 认证信息
     * @return 操作结果，包含文件列表或错误信息
     */
    @GetMapping("/getFilesList")
    public Result getFilesList(Authentication authentication) {
        return authentication == null || authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty() ?
                Result.failure(404, "token is invalid") :
                Result.success(BeanConvertUtil.convertList(fileService.getFilesListByRole(authentication.getPrincipal().toString()), FilesProcessVO.class));
//                Result.success(fileService.getFilesListByRole(authentication.getPrincipal().toString()));
    }


    /**
     * 上传文件接口
     * @param authentication 认证信息
     * @param file 上传的文件
     * @return 操作结果，包含上传后的文件信息或错误信息
     */
    @PostMapping("/upload")
    public Result uploadFile(Authentication authentication, @RequestParam("file") MultipartFile file) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return Result.failure(401, "token is invalid");
        }
        String uploaderId = authentication.getPrincipal().toString();
        try {
            FilesProcessDTO dto = fileService.uploadFile(file, uploaderId);
            return Result.success(BeanConvertUtil.convert(dto, FilesProcessVO.class));
        } catch (IOException | IllegalArgumentException e) {
            return Result.failure(500, e.getMessage());
        }
    }

    /**
     * 下载文件接口
     * @param authentication 认证信息
     * @param id 文件ID
     * @return 响应体，包含文件资源或错误状态码
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(Authentication authentication, @PathVariable String id) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }

        FilesProcessDTO dto = fileService.getFileById(id);
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

    /**
     * 分享文件接口
     * @param fileId 文件ID
     * @return 操作结果，包含分享链接信息或错误信息
     */
    @PostMapping("/{fileId}/share")
    public Result shareFile(@PathVariable String fileId) {
        return fileService.shareFile(fileId);
    }

    /**
     * 通过分享链接下载文件
     * @param id 分享链接ID
     * @return 响应体，包含文件资源或错误状态码（404未找到，410已过期等）
     */
    @GetMapping("/share/download/{id}")
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

    /**
     * 删除文件接口
     * @param fileId 文件ID
     * @return 操作结果，成功或失败信息
     */
    @PostMapping("/{fileId}/del")
    public Result deleteFile(@PathVariable String fileId) {
        return fileService.deleteFile(fileId);
    }


}
