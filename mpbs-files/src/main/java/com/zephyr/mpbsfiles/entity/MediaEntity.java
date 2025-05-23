package com.zephyr.mpbsfiles.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;


@TableName("medias")
@Data
public class MediaEntity {
    private String id;
    private String filename;
    private String storagePath;
    private String uploaderId;
    private String createdAt;
    private String size;
    private String mimeType;
    private String isPublic;
}
