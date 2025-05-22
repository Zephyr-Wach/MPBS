package com.zephyr.mpbsfiles.entity;

import lombok.Data;

import java.util.Date;

@Data
public class FilesEntity {
    private String id;
    private String filename;
    private String storagePath;
    private String uploaderId;
    private Date createdAt;
    private String size;
    private String mimeType;
    private Boolean isPublic;
}
///    `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
///   `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
///   `storage_path` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
///   `uploader_id` int NULL DEFAULT NULL,
///   `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
///   `size` bigint NULL DEFAULT NULL,
///   `mime_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
///   `is_public` tinyint(1) NULL DEFAULT 0,