package com.zephyr.mpbsuser.service;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsuser.dto.*;
import com.zephyr.mpbsuser.vo.UserInfoVO;

import java.util.List;

public interface UserService {

    /**
     * 用户登录
     * @param loginDTO 登录数据
     * @return 登录结果
     */
    Result login( LoginDTO loginDTO);

    /**
     * 用户邮箱登陆
     * @return loginDTO 登录数据
     */
    Result emailLogin(String email);

    /**
     * 用户注册
     * @param registerDTO 注册数据
     * @return 注册结果
     */
    Result register(RegisterDTO registerDTO);

    /**
     * 检查用户名是否可用
     * @param userName 用户名
     * @return 检查结果
     */
    Result checkByUserName(String userName) ;

    /**
     * 修改密码
     * @param updatePassword 修改密码数据
     * @return 修改结果
     */
    Result updatePassword(UpdatePasswordDTO updatePassword);

    /**
     * 获取用户信息
     * @return 用户信息
     */
    Result getUserInfo();

    /**
     * 修改用户信息
     * @param userId 用户id
     * @param info 修改信息
     * @return
     */
    boolean updateInfo(String userId, UpdateInfoDTO info);

    /**
     * 获取所有用户信息
     * @return 所有用户信息
     */
    List<UserInfoVO> getAllUsers();

    /**
     * 修改用户信息
     * @param userId 用户id
     * @param info 修改信息
     * @return 修改结果
     */
    boolean updateUserInfo(String userId, UserInfoDTO info);

    /**
     * 修改邮箱状态
     * @param userId 用户id
     * @param email 邮箱
     * @param status 状态
     * @return 修改结果
     */
    boolean updateEmailStatus(String userId, String email, String status);
}
