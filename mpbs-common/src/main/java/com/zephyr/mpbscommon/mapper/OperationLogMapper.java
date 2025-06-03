package com.zephyr.mpbscommon.mapper;

import com.zephyr.mpbscommon.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志 Mapper 接口
 */
@Mapper
public interface OperationLogMapper {

    /**
     * 插入操作日志记录
     * @param operationLog 操作日志实体
     * @return 受影响行数
     */
    int insert(OperationLog operationLog);
}
