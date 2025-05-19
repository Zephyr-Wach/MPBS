package com.zephyr.mpbsuser.dto;


import lombok.Data;

@Data
public class UpdatePasswordDTO {
    private String userId;
    private String oldPassword;
    private String newPassword;
}
