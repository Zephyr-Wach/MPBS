package com.zephyr.mpbsuser.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private Long userId;
    private String userName;
    private String userPwd;
    private String email;
}
