package com.zephyr.mpbsfiles.mapper;

import com.zephyr.mpbsfiles.dto.FilesProcessDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface FileMapper {
    List<FilesProcessDTO> getAccessibleFilesByRole(@Param("roleId") int roleId);

    // 插入文件信息，返回影响行数
    int insertFile(FilesProcessDTO file);

    // 根据id查询文件信息
    FilesProcessDTO selectFileById(@Param("id") String id);

}
