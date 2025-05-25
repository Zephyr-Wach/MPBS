package com.zephyr.mpbsuser.controller;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsuser.dto.UserInfoDTO;
import com.zephyr.mpbsuser.service.UserService;
import com.zephyr.mpbsuser.vo.UserInfoVO;
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
     * @return user list
     */
    @GetMapping("/getUserList")
    public Result getUserList() {
        List<UserInfoVO> users = userService.getAllUsers();
        return Result.success(users);
    }

    /**
     * update user info
     * @param userId user id
     * @param info user info
     * @return result
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
