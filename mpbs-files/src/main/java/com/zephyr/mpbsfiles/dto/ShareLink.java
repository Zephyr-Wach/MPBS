package com.zephyr.mpbsfiles.dto;

import lombok.Data;

@Data
public class ShareLink {
    private String id;
    private String fileId;
    private String expiresAt;
    private String createdAt;
    private String url;
}
