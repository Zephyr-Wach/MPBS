package com.zephyr.mpbsuser.controller;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsuser.dto.checkEmailDTO;
import com.zephyr.mpbsuser.mapper.UserMapper;
import com.zephyr.mpbsuser.service.EmailService;
import com.zephyr.mpbsuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    /**
     * 发送验证码
     * @param email 邮箱
     * @return 发送结果
     */
    @GetMapping("/sendCode")
    public Result sendCode(String email) {
        return emailService.sendEmailCode(email) ? Result.success() : Result.failure(500, "发送失败,1分钟后在尝试");
    }

    /**
     * 校验邮箱
     * @param checkEmailDTO 邮箱和验证码
     * @return 校验结果
     */
    @PostMapping("/checkEmail")
    public Result checkEmail(@RequestBody checkEmailDTO checkEmailDTO, Authentication authentication) {
        return emailService.verifyEmailCode(checkEmailDTO.getEmail(), checkEmailDTO.getCode())?
                (userService.updateEmailStatus(authentication.getPrincipal().toString(), checkEmailDTO.getEmail(), "confirmed")?
                        Result.success():
                        Result.failure(500, "邮箱验证失败")):
                Result.failure(400, "验证码错误");
    }

    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @return 检查结果
     */
    @GetMapping("/checkEmailExist")
    public Result checkEmailExist(String email) {
        return userMapper.findByEmail(email) != null?
                Result.success():
                Result.failure(500, "邮箱已注册");
    }

}
