package com.zephyr.mpbsuser.controller;

import com.zephyr.mpbscommon.annotation.LogOperation;
import com.zephyr.mpbsuser.dto.EmailLoginRequest;
import com.zephyr.mpbsuser.dto.LoginDTO;
import com.zephyr.mpbsuser.dto.RegisterDTO;
import com.zephyr.mpbsuser.service.EmailService;
import com.zephyr.mpbsuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zephyr.mpbscommon.utils.Result;

@RestController
@RequestMapping("/public/usr")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    /**
     * login
     * @param loginDTO 用户名+密码
     * @return Result
     */
    @PostMapping("/login")
    @LogOperation(operationType = "LOGIN")
    public Result login(@RequestBody LoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    /**
     * 邮箱登陆
     * @param request 邮箱+验证码
     * @return 登陆结果
     */
    @PostMapping("/loginByEmail")
    @LogOperation(operationType = "邮箱登陆")
    public Result loginByEmail(@RequestBody EmailLoginRequest request) {
        return emailService.verifyEmailCode(request.getEmail(), request.getCode())?
                userService.emailLogin(request.getEmail()):
                Result.failure(400, "验证码错误");
    }

    /**
     * register
     * @param registerDTO 注册信息
     * @return Result
     */
    @PostMapping("/register")
    @LogOperation(operationType = "REGISTER")
    public Result register(@RequestBody RegisterDTO registerDTO){
        return userService.register(registerDTO);
    }

    /**
     * checkUserName
     * @param userName 用户名
     * @return Result
     */
    @GetMapping("/checkUserName")
    @LogOperation(operationType = "检查用户名")
    public Result getUserInfo(@RequestParam String userName){return userService.checkByUserName(userName);}
}
