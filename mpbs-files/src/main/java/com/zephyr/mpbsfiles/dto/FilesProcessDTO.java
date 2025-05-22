package com.zephyr.mpbsfiles.dto;

import lombok.Data;

@Data
public class FilesProcessDTO {
    private String id;
    private String filename;
    private String storagePath;
    private String uploaderId;
    private String createdAt;
    private String size;
    private String mimeType;
}
