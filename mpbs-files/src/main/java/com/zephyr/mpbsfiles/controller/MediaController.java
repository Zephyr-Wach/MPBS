package com.zephyr.mpbsfiles.controller;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsfiles.dto.MediaProcessDTO;
import com.zephyr.mpbsfiles.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;

@RestController
@RequestMapping("/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    /**
     * upload picture/video
     */
    @PostMapping("/upload")
    public Result uploadFile(Authentication authentication, @RequestParam("file") MultipartFile file) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return Result.failure(401, "token is invalid");
        }
        String uploaderId = authentication.getPrincipal().toString();
        try {
            MediaProcessDTO dto = mediaService.uploadMedia(file, uploaderId);
            return Result.success(dto);
        } catch (IOException | IllegalArgumentException e) {
            return Result.failure(500, e.getMessage());
        }
    }

//    /**
//     * delete picture/video
//     */
//    @RequestMapping("/delete")
//    public String delete() {
//
//    }

    @GetMapping("/generateUrl/{mediaId}")
    public Result generateMediaUrl(@PathVariable("mediaId") String mediaId) {
        MediaProcessDTO dto = mediaService.getMediaById(mediaId);
        if (dto == null) {
            return Result.failure(404, "Media not found");
        }

        // 从完整路径中提取文件名
        String fileName = Paths.get(dto.getStoragePath()).getFileName().toString();
        String url = "/static/" + fileName;

        return Result.success(url);
    }

}
