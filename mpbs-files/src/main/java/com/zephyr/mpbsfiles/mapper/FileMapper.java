package com.zephyr.mpbsfiles.mapper;

import com.zephyr.mpbsfiles.dto.FilesProcessDTO;
import com.zephyr.mpbsfiles.entity.FilesEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface FileMapper {
    /**
     * 根据角色ID获取该角色可访问的文件列表
     * @param roleId 角色ID
     * @return 文件处理DTO列表
     */
    List<FilesProcessDTO> getAccessibleFilesByRole(@Param("roleId") int roleId);

    /**
     * 插入文件信息
     * @param file 文件处理DTO
     * @return 影响的行数
     */
    int insertFile(FilesProcessDTO file);

    /**
     * 根据文件ID查询文件信息
     * @param id 文件ID
     * @return 文件处理DTO
     */
    FilesProcessDTO selectFileById(@Param("id") String id);

    /**
     * 根据文件ID删除文件信息
     * @param fileId 文件ID
     */
    void deleteById(String fileId);

}
