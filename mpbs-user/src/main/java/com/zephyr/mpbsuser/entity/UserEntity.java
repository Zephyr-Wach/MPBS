package com.zephyr.mpbsuser.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 自动生成 getter/setter、toString 等
@NoArgsConstructor // 无参构造器
@AllArgsConstructor // 全参构造器
public class UserEntity {
    private String userId;
    private String userName;
    private String userPwd;
    private String email;
    private String avatarUrl;
    private String userPermission;
}
