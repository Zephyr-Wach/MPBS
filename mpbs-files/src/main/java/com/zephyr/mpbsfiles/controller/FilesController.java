package com.zephyr.mpbsfiles.controller;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsfiles.dto.FilesProcessDTO;
import com.zephyr.mpbsfiles.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    public ResponseEntity<?> downloadFile(Authentication authentication, @PathVariable String id) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).body("token is invalid");
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
            byte[] data = Files.readAllBytes(file.toPath());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dto.getFilename() + "\"")
                    .contentType(MediaType.parseMediaType(dto.getMimeType()))
                    .body(data);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("读取文件失败");
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
