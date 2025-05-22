package com.zephyr.mpbsfiles.service;

import com.zephyr.mpbsfiles.dto.MediaProcessDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MediaService {
    MediaProcessDTO uploadMedia(MultipartFile file, String uploaderId) throws IOException;

    MediaProcessDTO getMediaById(String mediaId);
}
