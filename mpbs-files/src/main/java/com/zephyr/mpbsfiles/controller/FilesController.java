package com.zephyr.mpbsfiles.controller;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsfiles.dto.FilesProcessDTO;
import com.zephyr.mpbsfiles.service.FileService;
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

@RestController
@RequestMapping("/files")
public class FilesController {

    @Autowired
    private FileService fileService;

    /**
     * get files list by token
     *
     */
    @GetMapping("/getFilesList")
    public Result getFilesList(Authentication authentication) {
        return authentication == null || authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty() ?
                Result.failure(404, "token is invalid") :
                Result.success(fileService.getFilesListByRole(authentication.getPrincipal().toString()));
    }

    /**
     * upload file
     */
    @PostMapping("/upload")
    public Result uploadFile(Authentication authentication, @RequestParam("file") MultipartFile file) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return Result.failure(401, "token is invalid");
        }
        String uploaderId = authentication.getPrincipal().toString();
        try {
            FilesProcessDTO dto = fileService.uploadFile(file, uploaderId);
            return Result.success(dto);
        } catch (IOException | IllegalArgumentException e) {
            return Result.failure(500, e.getMessage());
        }
    }

    /**
     * download file
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(Authentication authentication, @PathVariable String id) {
        // 鉴权校验
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }

        // 根据id获取文件信息
        FilesProcessDTO dto = fileService.getFileById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        File file = new File(dto.getStoragePath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        try {
            // 文件流资源
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            // 解决文件名中文乱码，URL编码后替换空格
            String encodedFilename = URLEncoder.encode(dto.getFilename(), StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType(dto.getMimeType()))
                    .body(resource);

        } catch (IOException e) {
            // 读取文件异常
            return ResponseEntity.status(500).build();
        }
    }


//    /**
//     * delete file
//     */
//    @RequestMapping("/delete")
//    public Result deleteFile(Authentication authentication) {
//        return authentication == null || authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty() ?
//                Result.failure(404, "token is invalid") :
//                Result.success();
//    }
//
//    /**
//     * share file
//     */
//    @RequestMapping("/share")
//    public Result shareFile(Authentication authentication) {
//        return authentication == null || authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty() ?
//                Result.failure(404, "token is invalid") :
//                Result.success( );
//    }
//
//    /**
//     * update file
//     */
//    @RequestMapping("/update")
//    public Result updateFile(Authentication authentication) {
//        return authentication == null || authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty() ?
//                Result.failure(404, "token is invalid") :
//                Result.success( );
//    }
}
