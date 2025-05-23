package com.zephyr.mpbsfiles.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyr.mpbsfiles.dto.MediaProcessDTO;
import com.zephyr.mpbsfiles.entity.MediaEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MediaMapper extends BaseMapper<MediaEntity> {
    // 插入文件信息，返回影响行数
    int insertMedia(MediaProcessDTO file);

    MediaProcessDTO getMediaById(String id);
}
