package com.zephyr.mpbsuser.controller;

import com.zephyr.mpbsuser.dto.LoginDTO;
import com.zephyr.mpbsuser.dto.RegisterDTO;
import com.zephyr.mpbsuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbssecurity.utils.PasswordUtils;

@RestController
@RequestMapping("/usr")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * login
     * @param loginDTO 用户名+密码
     * @return Result
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    /**
     * register
     * @param registerDTO 注册信息
     * @return Result
     */
    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO){
        return userService.register(registerDTO);
    }

    /**
     * checkUserName
     * @param userName 用户名
     * @return Result
     */
    @GetMapping("/checkUserName")
    public Result getUserInfo(@RequestParam String userName){return userService.checkByUserName(userName);}
}
