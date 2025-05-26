package com.zephyr.mpbsuser.service.Impl;

import com.zephyr.mpbsuser.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class EmailServiceImpl implements EmailService {
    @Value("${spring.mail.username}")
    private String fromEmail;


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String EMAIL_CODE_KEY_PREFIX = "email:code:";
    private static final String EMAIL_LIMIT_KEY_PREFIX = "email:limit:";

    @Override
    public boolean sendEmailCode(String email) {
        // 1. 限制发送频率：每个邮箱 60 秒内只能发一次
        String limitKey = EMAIL_LIMIT_KEY_PREFIX + email;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(limitKey))) {
            return false; // 已发送，拒绝重复发送
        }

        // 2. 生成验证码（6 位数字）
        String code = String.valueOf((int)((Math.random() * 9 + 1) * 100000));

        // 3. 存入 Redis（有效期 5 分钟）
        String codeKey = EMAIL_CODE_KEY_PREFIX + email;
        redisTemplate.opsForValue().set(codeKey, code, 5, TimeUnit.MINUTES);

        // 4. 设置限流 key（60 秒）
        redisTemplate.opsForValue().set(limitKey, "1", 60, TimeUnit.SECONDS);

        // 5. 构建并发送邮件
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("【验证码】来自MPBS");
            message.setText("您的验证码是：" + code + "。\n有效期为5分钟，请尽快使用！");
            message.setFrom(fromEmail);

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean verifyEmailCode(String email, String code) {
        if (email == null || code == null) return false;

        String codeKey = EMAIL_CODE_KEY_PREFIX + email;
        String storedCode = redisTemplate.opsForValue().get(codeKey);

        if (code.equals(storedCode)) {
            redisTemplate.delete(codeKey); // 验证通过后清除验证码
            return true;
        }
        return false;
    }
}
