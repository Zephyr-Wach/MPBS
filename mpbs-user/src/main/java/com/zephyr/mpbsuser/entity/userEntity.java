package com.zephyr.mpbsuser.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 自动生成 getter/setter、toString 等
@NoArgsConstructor // 无参构造器
@AllArgsConstructor // 全参构造器
public class userEntity {
    private String id;
    private String username;
    private String password;

}
