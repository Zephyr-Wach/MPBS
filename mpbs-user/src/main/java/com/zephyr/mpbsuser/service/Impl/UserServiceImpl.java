package com.zephyr.mpbsuser.service.Impl;

import com.zephyr.mpbscommon.utils.BeanConvertUtil;
import com.zephyr.mpbscommon.utils.JwtUtil;
import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsuser.dto.*;
import com.zephyr.mpbsuser.entity.UserEntity;
import com.zephyr.mpbsuser.mapper.UserMapper;
import com.zephyr.mpbsuser.service.UserService;
import com.zephyr.mpbsuser.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result login(LoginDTO loginDTO) {
        UserEntity user = userMapper.findByUserNameAndUserPwd(loginDTO.getUserName(), loginDTO.getUserPwd());
        return user != null ?
                Result.success(new HashMap<String, Object>() {{
                    put("token", JwtUtil.generateToken(user.getUserId(),user.getUserPermission()));
                    put("username", user.getUserName());
                    put("userId", user.getUserId());
                }}) :
                Result.failure(400, "用户名或密码错误");
    }

    @Override
    public Result emailLogin(String email) {
        UserEntity user = userMapper.findByEmail(email);
        return Objects.equals(user.getEmailStatus(), "confirmed") ?
                Result.success(new HashMap<String, Object>() {{
                    put("token", JwtUtil.generateToken(user.getUserId(),user.getUserPermission()));
                    put("username", user.getUserName());
                    put("userId", user.getUserId());
                }}) :
                Result.failure(400, "邮箱未验证，无法登陆");
    }

    @Override
    public Result register(RegisterDTO registerDTO) {
        userMapper.insert(registerDTO);
        UserEntity user = userMapper.findByUserName(registerDTO.getUserName());
        return user != null ?
                Result.success(new HashMap<String, Object>() {{
                    put("username", user.getUserName());
                    put("userId", user.getUserId());
                }}) :
                Result.failure(400, "注册失败");
    }

    @Override
    public Result checkByUserName(String userName) {
        return userMapper.countByUserName(userName) == 0?
                Result.success() :
                Result.failure(400, "用户名已存在");
    }

    @Override
    public Result updatePassword(UpdatePasswordDTO updatePassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Result.failure(401, "未认证或身份信息缺失");
        }

        String userId = (String) authentication.getPrincipal();

        // 1. 校验传来的userId和token中的userId是否一致
        if (!Objects.equals(userId, updatePassword.getUserId())) {
            return Result.failure(403, "无权限修改其他用户密码");
        }

        // 2. 校验旧密码是否正确（建议用userId查询用户，再比对密码）
        UserEntity user = userMapper.findByUserIdAndUserPwd(updatePassword.getUserId(), updatePassword.getOldPassword());
        if (user == null) {
            return Result.failure(400, "旧密码错误");
        }

        // 3. 执行密码更新
        boolean success = userMapper.updatePassword(updatePassword);
        if (success) {
            return Result.success();
        } else {
            return Result.failure(500, "密码修改失败，请稍后重试");
        }
    }

    @Override
    public Result getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null || !authentication.isAuthenticated()?
                Result.failure(401, "未认证或身份信息缺失"):
                Result.success(BeanConvertUtil.convert( userMapper.findByUserId((String) authentication.getPrincipal()), UserInfoVO.class ));
    }

    @Override
    public boolean updateInfo(String userId, UpdateInfoDTO info) {
        return userMapper.updateInfo(userId, info);
    }

    @Override
    public List<UserInfoVO> getAllUsers() {
        return BeanConvertUtil.convertList(userMapper.findAllUsers(), UserInfoVO.class ) ;
    }

    @Override
    public boolean updateUserInfo(String userId, UserInfoDTO info) {
        return userMapper.updateUserInfo(userId, info);
    }

    @Override
    public boolean updateEmailStatus(String email, String status) {
        UserEntity user = userMapper.findByEmail(email);
        user.setEmailStatus(status);
        userMapper.updateEmailStatus(email, status);
        return Objects.equals(userMapper.findByEmail(email).getEmailStatus(), status);
    }
}
