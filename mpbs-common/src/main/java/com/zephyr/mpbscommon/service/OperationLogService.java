package com.zephyr.mpbscommon.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zephyr.mpbscommon.entity.OperationLog;

import java.util.Date;

/**
 * 操作日志服务接口
 */
public interface OperationLogService {

    /**
     * 保存操作日志
     * @param operationLog 操作日志实体
     */
    void save(OperationLog operationLog);

    /**
     *  获取操作日志
     * @param userAccount 用户账号
     * @param operationType 操作类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param page 页码
     * @param size 每页大小
     * @return  操作日志列表
     */
    public Page<OperationLog> getLogs(String userAccount, String operationType, java.util.Date startTime, Date endTime, int page, int size);
}
