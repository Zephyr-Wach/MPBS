package com.zephyr.mpbsuser.service;

public interface EmailService {
    /**
     * 发送验证码邮件
     * @param email
     * @return
     */
    boolean sendEmailCode(String email);

    /**
     * 验证邮件验证码
     * @param email
     * @param code
     * @return
     */
    boolean verifyEmailCode(String email, String code);
}
