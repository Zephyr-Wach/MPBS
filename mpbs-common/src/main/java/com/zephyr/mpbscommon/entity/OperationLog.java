package com.zephyr.mpbscommon.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("operation_log")
public class OperationLog {
    private Long id;
    private String userAccount;
    private Date operationTime;
    private String operationType;
    private String sourceIp;
    private String targetIp;
    private Integer sourcePort;
    private String clientHardware;
    private String operationDetail;
}
