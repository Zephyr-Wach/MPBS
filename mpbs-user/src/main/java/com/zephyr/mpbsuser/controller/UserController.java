package com.zephyr.mpbsuser.controller;

import com.zephyr.mpbsuser.dto.LoginDTO;
import com.zephyr.mpbsuser.dto.RegisterDTO;
import com.zephyr.mpbsuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zephyr.mpbscommon.utils.Result;

@RestController
@RequestMapping("/usr")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * login
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO) {
        return userService.login(loginDTO);
    }


    /**
     * register
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO){
        return userService.register(registerDTO);
    }

    /**
     * checkUserName
     * @return
     */
    @GetMapping("/checkUserName")
    public Result getUserInfo(@RequestParam String userName){
        return userService.checkByUserName(userName);
    }
}
