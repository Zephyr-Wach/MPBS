package com.zephyr.mpbsuser.controller;

import com.zephyr.mpbscommon.annotation.LogOperation;
import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsuser.dto.UpdateInfoDTO;
import com.zephyr.mpbsuser.dto.UpdatePasswordDTO;
import com.zephyr.mpbsuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/NORMAL/user")
public class UserWithTokenController {
    @Autowired
    private UserService userService;

    /**
     * update user password
     * @param updatePasswordDTO updatePasswordDTO
     * @return Result
     */
    @PostMapping("/updatePassword")
    @LogOperation(operationType = "更新密码")
    public Result updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        return userService.updatePassword(updatePasswordDTO);
    }

    /**
     * get info by token
     * @return Result
     */
    @GetMapping("/getInfoByToken")
    @LogOperation(operationType = "获取用户信息")
    public Result getInfoByToken() {
        return userService.getUserInfo();
    }

    /**
     * update user info
     * @param info info
     * @return Result
     */
    @PostMapping("/updateInfo")
    @LogOperation(operationType = "更新用户信息")
    public Result updateInfo(@RequestBody UpdateInfoDTO info , Authentication authentication) {
        return authentication == null || authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty() ?
                Result.failure(404, "token is invalid") :
                Result.success(userService.updateInfo(authentication.getPrincipal().toString(),  info));
    }
}
