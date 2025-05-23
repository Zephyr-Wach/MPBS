package com.zephyr.mpbsuser.controller;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsuser.dto.UpdateInfoDTO;
import com.zephyr.mpbsuser.dto.UpdatePasswordDTO;
import com.zephyr.mpbsuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/user")
public class UserWithTokenController {
    @Autowired
    private UserService userService;

    /**
     * update user password
     * @param updatePasswordDTO
     * @return
     */
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        return userService.updatePassword(updatePasswordDTO);
    }

    /**
     * get info by token
     * @return
     */
    @GetMapping("/getInfoByToken")
    public Result getInfoByToken() {
        return userService.getUserInfo();
    }

    /**
     * update user info
     */
    @PostMapping("/updateInfo")
    public Result updateInfo(@RequestBody UpdateInfoDTO info , Authentication authentication) {
        return authentication == null || authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty() ?
                Result.failure(404, "token is invalid") :
                Result.success(userService.updateInfo(authentication.getPrincipal().toString(),  info));
    }
}
