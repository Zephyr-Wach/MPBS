package com.zephyr.mpbsfiles.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface FilePermissionMapper {

    /**
     * 判断指定角色是否有删除指定文件的权限
     * @param fileId 文件ID
     * @param roleId 角色ID
     * @return 是否有删除权限
     */
    boolean hasDeletePermission(@Param("fileId") String fileId, @Param("roleId") Integer roleId);

    /**
     * 根据文件ID删除文件信息
     * @param fileId 文件ID
     */
    void deleteByFileId(@Param("fileId") String fileId);

}
