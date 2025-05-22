package com.zephyr.mpbsfiles.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TemporaryLinksEntity {
    private String id;
    private String fileId;
    private Date expiresAt;
    private Date createdAt;
}
///   `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'uuid()',
///   `file_id` bigint UNSIGNED NOT NULL,
///   `expires_at` datetime NOT NULL,
///   `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,