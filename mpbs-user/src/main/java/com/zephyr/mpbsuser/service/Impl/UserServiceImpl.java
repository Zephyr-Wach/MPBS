package com.zephyr.mpbsuser.service.Impl;

import com.zephyr.mpbscommon.utils.JwtUtil;
import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsuser.dto.LoginDTO;
import com.zephyr.mpbsuser.dto.RegisterDTO;
import com.zephyr.mpbsuser.dto.UpdatePasswordDTO;
import com.zephyr.mpbsuser.entity.UserEntity;
import com.zephyr.mpbsuser.mapper.UserMapper;
import com.zephyr.mpbsuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.HashMap;
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
                    put("token", JwtUtil.generateToken(user.getUserId()));
                    put("username", user.getUserName());
                    put("userId", user.getUserId());
                }}) :
                Result.failure(400, "用户名或密码错误");
    }

    @Override
    public Result register(RegisterDTO registerDTO) {
        userMapper.insert(registerDTO);
        UserEntity user = userMapper.findByUserName(registerDTO.getUserName());
        return user != null ?
                Result.success(new HashMap<String, Object>() {{
                    put("token", JwtUtil.generateToken(user.getUserId()));
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
            return Result.failure(401,"未认证或身份信息缺失");
        }

        String userId = (String) authentication.getPrincipal();

        return Objects.equals(userId, updatePassword.getUserId()) &&
                userMapper.findByUserNameAndUserPwd(updatePassword.getUserId(), updatePassword.getOldPassword()) != null &&
                userMapper.updatePassword(updatePassword)?
                Result.success() :
                Result.failure(400, "修改失败");
    }
}
