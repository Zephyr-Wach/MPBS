package com.zephyr.mpbsuser.mapper;

import com.zephyr.mpbsuser.dto.RegisterDTO;
import com.zephyr.mpbsuser.dto.UpdatePasswordDTO;
import com.zephyr.mpbsuser.entity.UserEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.query.Param;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user_info WHERE user_name = #{userName}")
    UserEntity findByUserName(@Param("username") String userName);

    @Select("SELECT * FROM user_info WHERE user_name = #{userName} AND user_pwd = #{userPwd}")
    UserEntity findByUserNameAndUserPwd(@Param("userName") String userName, @Param("userPwd") String userPwd);

    @Select("SELECT * FROM user_info WHERE user_id = #{userId} AND user_pwd =#{userPwd}")
    UserEntity findByUserIdAndUserPwd(@Param("userId") String userId, @Param("userPwd") String userPwd);

    @Insert("INSERT INTO user_info (user_name, user_pwd, email) VALUES (#{userName}, #{userPwd}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    void insert(RegisterDTO registerDTO);

    @Select("SELECT COUNT(1) FROM user_info WHERE user_name = #{userName}")
    int countByUserName(String userName);

    @Update("UPDATE user_info SET user_pwd = #{newPassword} WHERE user_id = #{userId}")
    boolean updatePassword(UpdatePasswordDTO updatePassword);
}
