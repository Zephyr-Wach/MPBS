package com.zephyr.mpbsfiles.mapper;

import com.zephyr.mpbsfiles.dto.MediaProcessDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MediaMapper {
    // 插入文件信息，返回影响行数
    int insertMedia(MediaProcessDTO file);

    MediaProcessDTO getMediaById(String id);
}
