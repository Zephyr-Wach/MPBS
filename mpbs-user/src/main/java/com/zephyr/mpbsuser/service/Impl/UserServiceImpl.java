package com.zephyr.mpbsuser.service.Impl;

import com.zephyr.mpbscommon.utils.JwtUtil;
import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsuser.dto.LoginDTO;
import com.zephyr.mpbsuser.dto.RegisterDTO;
import com.zephyr.mpbsuser.entity.UserEntity;
import com.zephyr.mpbsuser.mapper.UserMapper;
import com.zephyr.mpbsuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

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
}
