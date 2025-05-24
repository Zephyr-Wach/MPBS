package com.zephyr.mpbsuser.dto;

import lombok.Data;

@Data
public class UserInfoDTO {
    private String userName;
    private String userPwd;        // 管理员可修改密码
    private String email;
    private String avatarUrl;
    private String userPermission; // 权限字段
}