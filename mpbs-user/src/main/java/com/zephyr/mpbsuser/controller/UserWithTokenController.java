package com.zephyr.mpbsuser.controller;

import com.zephyr.mpbsuser.dto.UpdatePasswordDTO;
import com.zephyr.mpbsuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Object updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        return userService.updatePassword(updatePasswordDTO);
    }
}
