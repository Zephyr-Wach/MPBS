package com.zephyr.mpbsfiles.controller;

import com.zephyr.mpbscommon.utils.BeanConvertUtil;
import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsfiles.dto.MediaProcessDTO;
import com.zephyr.mpbsfiles.service.MediaService;
import com.zephyr.mpbsfiles.vo.MediaProcessVO;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    /**
     * 上传图片或视频
     * @param authentication 认证信息
     * @param file 上传的文件
     * @return 操作结果，包含上传后的媒体信息或错误信息
     */
    @PostMapping("/upload")
    public Result uploadFile(Authentication authentication, @RequestParam("file") MultipartFile file) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return Result.failure(401, "token is invalid");
        }
        String uploaderId = authentication.getPrincipal().toString();
        try {
            MediaProcessDTO dto = mediaService.uploadMedia(file, uploaderId);
            return Result.success(BeanConvertUtil.convert(dto, MediaProcessVO.class));
        } catch (IOException | IllegalArgumentException e) {
            return Result.failure(500, e.getMessage());
        }
    }

    /**
     * 生成媒体文件访问URL
     * @param mediaId 媒体文件ID
     * @return 操作结果，包含媒体访问URL或错误信息
     */
    @GetMapping("/generateUrl/{mediaId}")
    public Result generateMediaUrl(@PathVariable("mediaId") String mediaId) {
        MediaProcessDTO dto = mediaService.getMediaById(mediaId);
        if (dto == null) {
            return Result.failure(404, "Media not found");
        }

        String fileName = Paths.get(dto.getStoragePath()).getFileName().toString();
        String url = "/static/" + fileName;

        return Result.success(url);
    }

    /**
     * 删除图片或视频
     * @param authentication 认证信息
     * @param mediaId 媒体文件ID
     * @return 操作结果，成功或失败信息（无权限或文件不存在）
     */
    @DeleteMapping("/delete")
    public Result deleteMedia(Authentication authentication, @RequestParam("mediaId") String mediaId) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return Result.failure(401, "token is invalid");
        }
        String userId = authentication.getPrincipal().toString();
        boolean success = mediaService.deleteMedia(mediaId, userId);
        return success ? Result.success("Delete successful") : Result.failure(403, "No permission or media not found");
    }

    /**
     * 获取当前用户的图片/视频列表，支持分页
     * @param authentication 认证信息
     * @param page 当前页码，默认1
     * @param size 每页数量，默认10
     * @return 操作结果，包含分页后的媒体列表
     */
    @RequestMapping("/list")
    public Result listMedia(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (authentication == null || authentication.getPrincipal() == null) {
            return Result.failure(401, "token is invalid");
        }

        String userId = authentication.getPrincipal().toString();
        return Result.success(mediaService.listMediaByUser(userId, page, size));
    }

}
