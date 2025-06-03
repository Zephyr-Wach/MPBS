package com.zephyr.mpbscommon.aspect;

import com.zephyr.mpbscommon.annotation.LogOperation;
import com.zephyr.mpbscommon.entity.OperationLog;
import com.zephyr.mpbscommon.service.OperationLogService;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

@Aspect
@Component
public class OperationLogAspect {

    private final OperationLogService operationLogService;
    private final HttpServletRequest request;

    public OperationLogAspect(OperationLogService operationLogService, HttpServletRequest request) {
        this.operationLogService = operationLogService;
        this.request = request;
    }

    @Pointcut("@annotation(com.zephyr.mpbscommon.annotation.LogOperation)")
    public void logPointcut() {}

    @AfterReturning(pointcut = "logPointcut()", returning = "retVal")
    public void recordLog(JoinPoint joinPoint, Object retVal) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            LogOperation logOperation = method.getAnnotation(LogOperation.class);
            String operationType = logOperation.operationType();

            String userAccount = getCurrentUsername();
            if (userAccount == null) {
                userAccount = "匿名用户";
            }

            String sourceIp = getClientIP();
            String targetIp = request.getLocalAddr();
            Integer sourcePort = request.getRemotePort();
            String clientHardware = request.getHeader("User-Agent");
            String operationDetail = buildOperationDetail(joinPoint, retVal);

            OperationLog log = new OperationLog();
            log.setUserAccount(userAccount);
            log.setOperationTime(new Date());
            log.setOperationType(operationType);
            log.setSourceIp(sourceIp);
            log.setTargetIp(targetIp);
            log.setSourcePort(sourcePort);
            log.setClientHardware(clientHardware);
            log.setOperationDetail(operationDetail);

            operationLogService.save(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            return authentication.getName();
        }
        return null;
    }

    private String getClientIP() {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    private String buildOperationDetail(JoinPoint joinPoint, Object retVal) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        return "调用方法：" + methodName + "，参数：" + Arrays.toString(args);
    }
}