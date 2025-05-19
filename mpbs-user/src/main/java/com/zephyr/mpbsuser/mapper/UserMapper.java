package com.zephyr.mpbsuser.mapper;

import com.zephyr.mpbsuser.dto.RegisterDTO;
import com.zephyr.mpbsuser.entity.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user_info WHERE user_name = #{userName}")
    UserEntity findByUserName(@Param("username") String userName);

    @Select("SELECT * FROM user_info WHERE user_name = #{userName} AND user_pwd = #{userPwd}")
    UserEntity findByUserNameAndUserPwd(@Param("userName") String userName, @Param("userPwd") String userPwd);

    @Insert("INSERT INTO user_info (user_name, user_pwd, email) VALUES (#{userName}, #{userPwd}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    void insert(RegisterDTO registerDTO);

    @Select("SELECT COUNT(1) FROM user_info WHERE user_name = #{userName}")
    int countByUserName(String userName);
}
