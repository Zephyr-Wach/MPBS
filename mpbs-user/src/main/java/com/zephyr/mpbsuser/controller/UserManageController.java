package com.zephyr.mpbsuser.controller;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsuser.dto.UpdateInfoDTO;
import com.zephyr.mpbsuser.dto.UserInfoDTO;
import com.zephyr.mpbsuser.entity.UserEntity;
import com.zephyr.mpbsuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class UserManageController {
    @Autowired
    private UserService userService;

    /**
     * get user list
     */
    @GetMapping("/getUserList")
    public Result getUserList() {
        List<UserEntity> users = userService.getAllUsers();
        return Result.success(users);
    }

    /**
     * update user info
     */
    @PostMapping("/updateInfo")
    public Result updateInfo(@RequestParam String userId, @RequestBody UserInfoDTO info) {
        boolean success = userService.updateUserInfo(userId, info);
        if (success) {
            return Result.success("更新成功");
        } else {
            return Result.failure(400,"更新失败");
        }
    }


}
