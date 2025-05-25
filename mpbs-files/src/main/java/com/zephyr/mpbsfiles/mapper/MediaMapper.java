package com.zephyr.mpbsfiles.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyr.mpbsfiles.dto.MediaProcessDTO;
import com.zephyr.mpbsfiles.entity.MediaEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MediaMapper extends BaseMapper<MediaEntity> {

    /**
     * 插入媒体文件信息
     * @param file 媒体处理DTO
     * @return 影响的行数
     */
    int insertMedia(MediaProcessDTO file);

    /**
     * 根据媒体ID查询媒体文件信息
     * @param id 媒体文件ID
     * @return 媒体处理DTO
     */
    MediaProcessDTO getMediaById(String id);

}
