package com.zephyr.mpbsfiles.dto;

import lombok.Data;

@Data
public class FilePermissionDTO {
    private int roleId;
    private boolean canRead;
    private boolean canDownload;
    private boolean canUpdate;
    private boolean canDelete;
}

