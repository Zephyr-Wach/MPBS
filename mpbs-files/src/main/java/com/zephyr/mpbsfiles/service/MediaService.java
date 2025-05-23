package com.zephyr.mpbsfiles.service;

import com.zephyr.mpbsfiles.dto.MediaProcessDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MediaService {
    MediaProcessDTO uploadMedia(MultipartFile file, String uploaderId) throws IOException;

    MediaProcessDTO getMediaById(String mediaId);

    Map<String, Object> listMediaByUser(String userId, int page, int size);
    boolean deleteMedia(String mediaId, String userId);
}
