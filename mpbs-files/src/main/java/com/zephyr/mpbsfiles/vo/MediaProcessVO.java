package com.zephyr.mpbsfiles.vo;

import lombok.Data;

@Data
public class MediaProcessVO {
    private String id;
    private String filename;
    private String storagePath;
    private String uploaderId;
    private String createdAt;
    private String size;
    private String mimeType;
}
