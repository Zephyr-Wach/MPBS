package com.zephyr.mpbsuser.controller;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsuser.dto.checkEmailDTO;
import com.zephyr.mpbsuser.service.EmailService;
import com.zephyr.mpbsuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;

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
    public Result checkEmail(@RequestBody checkEmailDTO checkEmailDTO) {
        return emailService.verifyEmailCode(checkEmailDTO.getEmail(), checkEmailDTO.getCode())?
                (userService.updateEmailStatus(checkEmailDTO.getEmail(), "confirmed")?
                        Result.success():
                        Result.failure(500, "邮箱验证失败")):
                Result.failure(400, "验证码错误");
    }

}
