package com.zephyr.mpbscommon.controller;

import com.zephyr.mpbscommon.annotation.LogOperation;
import com.zephyr.mpbscommon.entity.OperationLog;
import com.zephyr.mpbscommon.service.OperationLogService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/JUNIOR/logs")
public class OperationLogController {

    private final OperationLogService operationLogService;

    public OperationLogController(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

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
    @GetMapping("/list")
    @LogOperation(operationType = "获取操作日志列表")
    public ResponseEntity<Page<OperationLog>> listLogs(
            @RequestParam(value = "userAccount", required = false) String userAccount,
            @RequestParam(value = "operationType", required = false) String operationType,
            @RequestParam(value = "startTime", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Timestamp startTimestamp = startTime == null ? null : Timestamp.valueOf(startTime);
        Timestamp endTimestamp = endTime == null ? null : Timestamp.valueOf(endTime);

        Page<OperationLog> result = operationLogService.getLogs(userAccount, operationType, startTimestamp, endTimestamp, page, size);
        return ResponseEntity.ok(result);
    }
}
