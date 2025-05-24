package com.zephyr.mpbsuser.service;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsuser.dto.*;
import com.zephyr.mpbsuser.entity.UserEntity;

import java.util.List;

public interface UserService {
    Result login( LoginDTO loginDTO);
    Result register(RegisterDTO registerDTO);
    Result checkByUserName(String userName) ;
    Result updatePassword(UpdatePasswordDTO updatePassword);
    Result getUserInfo();
    boolean updateInfo(String userId, UpdateInfoDTO info);

    // 新增获取所有用户列表
    List<UserEntity> getAllUsers();

    // 更新用户信息，参数见上面controller
    boolean updateUserInfo(String userId, UserInfoDTO info);
}
