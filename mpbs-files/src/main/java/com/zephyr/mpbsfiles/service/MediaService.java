package com.zephyr.mpbsfiles.service;

import com.zephyr.mpbsfiles.dto.MediaProcessDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MediaService {
    /**
     * 上传媒体文件
     * @param file 上传的媒体文件
     * @param uploaderId 上传者ID
     * @return 上传后媒体文件的DTO对象
     * @throws IOException 文件操作异常
     */
    MediaProcessDTO uploadMedia(MultipartFile file, String uploaderId) throws IOException;

    /**
     * 根据媒体ID查询媒体文件信息
     * @param mediaId 媒体文件ID
     * @return 媒体处理DTO
     */
    MediaProcessDTO getMediaById(String mediaId);

    /**
     * 分页查询指定用户的媒体文件列表
     * @param userId 用户ID
     * @param page 当前页码
     * @param size 每页条数
     * @return 包含媒体文件列表和分页信息的Map
     */
    Map<String, Object> listMediaByUser(String userId, int page, int size);

    /**
     * 删除指定用户的媒体文件
     * @param mediaId 媒体文件ID
     * @param userId 用户ID（用于权限校验）
     * @return 是否删除成功
     */
    boolean deleteMedia(String mediaId, String userId);

}
