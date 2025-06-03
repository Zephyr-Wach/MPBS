package com.zephyr.mpbscommon.service.Impl;

import com.zephyr.mpbscommon.entity.OperationLog;
import com.zephyr.mpbscommon.mapper.OperationLogMapper;
import com.zephyr.mpbscommon.service.OperationLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Resource
    private OperationLogMapper operationLogMapper;

    @Override
    public void save(OperationLog operationLog) {
        operationLogMapper.insert(operationLog);
    }
}