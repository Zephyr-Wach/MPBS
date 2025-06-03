package com.zephyr.mpbscommon.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zephyr.mpbscommon.entity.OperationLog;
import com.zephyr.mpbscommon.mapper.OperationLogMapper;
import com.zephyr.mpbscommon.service.OperationLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Resource
    private OperationLogMapper operationLogMapper;

    @Override
    public void save(OperationLog operationLog) {
        operationLogMapper.insert(operationLog);
    }

    @Override
    public Page<OperationLog> getLogs(String userAccount, String operationType, Date startTime, Date endTime, int page, int size) {
        QueryWrapper<OperationLog> queryWrapper = new QueryWrapper<>();

        if (userAccount != null && !userAccount.isEmpty()) {
            queryWrapper.eq("user_account", userAccount);
        }
        if (operationType != null && !operationType.isEmpty()) {
            queryWrapper.eq("operation_type", operationType);
        }
        if (startTime != null) {
            queryWrapper.ge("operation_time", startTime);
        }
        if (endTime != null) {
            queryWrapper.le("operation_time", endTime);
        }

        queryWrapper.orderByDesc("operation_time");

        Page<OperationLog> pageRequest = new Page<>(page, size);

        return operationLogMapper.selectPage(pageRequest, queryWrapper);
    }
}
