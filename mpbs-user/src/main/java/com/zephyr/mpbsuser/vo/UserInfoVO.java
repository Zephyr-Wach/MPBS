package com.zephyr.mpbsuser.vo;

import lombok.Data;

@Data
public class UserInfoVO {
    private String userId;
    private String userName;
    private String email;
    private String avatarUrl;
    private String userPermission;
    private String emailStatus;
}
