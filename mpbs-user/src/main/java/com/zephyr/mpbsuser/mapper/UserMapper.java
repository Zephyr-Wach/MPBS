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

    @Select("SELECT * FROM user_info WHERE user_id = #{userId}")
    UserEntity findByUserId(String userId);

    @Update("UPDATE user_info SET user_name = #{info.userName}, avatar_url = #{info.avatarUrl}, email = #{info.email} WHERE user_id = #{userId}")
    boolean updateInfo(@Param("userId") String userId, @Param("info") UpdateInfoDTO info);

    @Select("SELECT * FROM user_info")
    List<UserEntity> findAllUsers();

    @Update({
            "<script>",
            "UPDATE user_info",
            "<set>",
            "<if test='info.userName != null'>user_name = #{info.userName},</if>",
            "<if test='info.userPwd != null'>user_pwd = #{info.userPwd},</if>",
            "<if test='info.email != null'>email = #{info.email},</if>",
            "<if test='info.avatarUrl != null'>avatar_url = #{info.avatarUrl},</if>",
            "<if test='info.userPermission != null'>user_permission = #{info.userPermission},</if>",
            "</set>",
            "WHERE user_id = #{userId}",
            "</script>"
    })
    boolean updateUserInfo(String userId, UserInfoDTO info);
}
