package com.zephyr.mpbsfiles.entity;

import lombok.Data;

@Data
public class FilePermissionsEntity {
    private String id;
    private String fileId;
    private String roleId;
    private Boolean canRead;
    private Boolean canDownload;
    private Boolean canUpdate;
    private Boolean canDelete;
    private Boolean canShare;
}
///   `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
///   `file_id` int NOT NULL,
///   `role_id` int NOT NULL,
///   `can_read` tinyint(1) NULL DEFAULT 0,
///   `can_download` tinyint(1) NULL DEFAULT 0,
///   `can_update` tinyint(1) NULL DEFAULT 0,
///   `can_delete` tinyint(1) NULL DEFAULT 0,