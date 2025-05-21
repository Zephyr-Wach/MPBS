package com.zephyr.mpbsuser.controller;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsuser.dto.UpdatePasswordDTO;
import com.zephyr.mpbsuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
