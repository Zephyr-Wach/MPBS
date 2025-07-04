package com.zephyr.mpbsfiles.controller;

import com.zephyr.mpbscommon.annotation.LogOperation;
import com.zephyr.mpbscommon.utils.BeanConvertUtil;
import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsfiles.dto.FilesProcessDTO;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/SENIOR/files")
public class FilesController {

    @Autowired
    private FileService fileService;

    /**
     * 根据当前用户的token获取该用户角色可访问的文件列表
     * @param authentication 认证信息
     * @return 操作结果，包含文件列表或错误信息
     */
    @GetMapping("/getFilesList")
    @LogOperation(operationType = "根据当前用户的token获取该用户角色可访问的文件列表")
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
    @LogOperation(operationType = "上传文件")
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
    @LogOperation(operationType = "下载文件")
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
    @LogOperation(operationType = "分享文件")
    public Result shareFile(@PathVariable String fileId) {
        return fileService.shareFile(fileId);
    }

    /**
     * 删除文件接口
     * @param fileId 文件ID
     * @return 操作结果，成功或失败信息
     */
    @PostMapping("/{fileId}/del")
    @LogOperation(operationType = "删除文件")
    public Result deleteFile(@PathVariable String fileId) {
        return fileService.deleteFile(fileId);
    }
}
