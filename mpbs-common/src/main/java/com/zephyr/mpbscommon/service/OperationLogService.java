package com.zephyr.mpbscommon.service;

import com.zephyr.mpbscommon.entity.OperationLog;

/**
 * 操作日志服务接口
 */
public interface OperationLogService {

    /**
     * 保存操作日志
     * @param operationLog 操作日志实体
     */
    void save(OperationLog operationLog);
}
