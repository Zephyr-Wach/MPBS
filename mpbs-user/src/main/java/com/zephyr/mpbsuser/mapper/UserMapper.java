package com.zephyr.mpbsuser.mapper;

import com.zephyr.mpbsuser.dto.RegisterDTO;
import com.zephyr.mpbsuser.dto.UpdateInfoDTO;
import com.zephyr.mpbsuser.dto.UpdatePasswordDTO;
import com.zephyr.mpbsuser.dto.UserInfoDTO;
import com.zephyr.mpbsuser.entity.UserEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    /**
     * 根据用户名查询用户信息
     * @param userName 用户名
     * @return 用户实体
     */
    UserEntity findByUserName(@Param("username") String userName);

    /**
     * 根据用户名和密码查询用户信息（用于登录验证）
     * @param userName 用户名
     * @param userPwd 用户密码
     * @return 用户实体
     */
    UserEntity findByUserNameAndUserPwd(@Param("userName") String userName, @Param("userPwd") String userPwd);

    /**
     * 根据用户ID和密码查询用户信息（用于登录验证）
     * @param userId 用户ID
     * @param userPwd 用户密码
     * @return 用户实体
     */
    UserEntity findByUserIdAndUserPwd(@Param("userId") String userId, @Param("userPwd") String userPwd);

    /**
     * 插入新用户注册信息
     * @param registerDTO 注册数据传输对象
     */
    void insert(RegisterDTO registerDTO);

    /**
     * 根据用户名统计用户数量（判断用户名是否存在）
     * @param userName 用户名
     * @return 用户数量
     */
    int countByUserName(String userName);

    /**
     * 更新用户密码
     * @param updatePassword 更新密码的数据传输对象
     * @return 更新是否成功
     */
    boolean updatePassword(UpdatePasswordDTO updatePassword);

    /**
     * 根据用户ID查询用户信息
     * @param userId 用户ID
     * @return 用户实体
     */
    UserEntity findByUserId(String userId);

    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param info 用户更新信息数据传输对象
     * @return 更新是否成功
     */
    boolean updateInfo(@Param("userId") String userId, @Param("info") UpdateInfoDTO info);

    /**
     * 查询所有用户列表
     * @return 用户实体列表
     */
    List<UserEntity> findAllUsers();

    /**
     * 更新用户详细信息
     * @param userId 用户ID
     * @param info 用户详细信息数据传输对象
     * @return 更新是否成功
     */
    boolean updateUserInfo(@Param("userId") String userId, @Param("info") UserInfoDTO info);

    /**
     * 根据邮箱查询用户信息
     * @param email 邮箱
     * @return 用户实体
     */
    UserEntity findByEmail(@Param("email") String email);

    /**
     * 更新用户邮箱状态
     * @param email  邮箱
     * @param status 状态
     * @return 更新是否成功
     */
    int updateEmailStatus(@Param("email") String email, @Param("status") String status);
}