package com.zephyr.mpbsfiles.controller;

import com.zephyr.mpbscommon.annotation.LogOperation;
import com.zephyr.mpbscommon.utils.BeanConvertUtil;
import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsfiles.dto.MediaProcessDTO;
import com.zephyr.mpbsfiles.service.MediaService;
import com.zephyr.mpbsfiles.vo.MediaProcessVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/NORMAL/media")
public class NORMALController {

    @Autowired
    private MediaService mediaService;

    /**
     * 上传头像
     * @param authentication 认证信息
     * @param file 上传的文件
     * @return 操作结果，包含上传后的媒体信息或错误信息
     */
    @PostMapping("/upload")
    @LogOperation(operationType = "上传图片或视频")
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
}
