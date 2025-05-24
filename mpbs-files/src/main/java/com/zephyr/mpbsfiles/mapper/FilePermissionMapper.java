package com.zephyr.mpbsfiles.mapper;


import com.zephyr.mpbsfiles.dto.FilePermissionDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

@Mapper
public interface FilePermissionMapper {
    @Select("SELECT can_delete FROM file_permissions WHERE file_id = #{fileId} AND role_id = #{roleId}")
    boolean hasDeletePermission(@Param("fileId") String fileId, @Param("roleId") Integer roleId);

    @Delete("DELETE FROM file_permissions WHERE file_id = #{fileId}")
    void deleteByFileId(@Param("fileId") String fileId);

}
