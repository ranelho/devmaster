package com.devmaster.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private static final String CONTROLLER_EXECUTION_MESSAGE = "Controller method: {} executed in {} ms";
    private static final String SERVICE_EXECUTION_MESSAGE = "Service method: {} executed in {} ms";
    private static final Set<String> SENSITIVE_FIELDS = Set.of(
        "password", "senha", "cpf", "cnpj", "cnh", "token", 
        "accessToken", "refreshToken", "authorization"
    );

    @Pointcut("execution(* com.devmaster.application.api..*(..))")
    public void controllerMethods() {
    }

    @Pointcut("execution(* com.devmaster.application.service.impl..*(..))")
    public void serviceMethods() {
    }

    @Before("controllerMethods()")
    public void logControllerEntry(JoinPoint joinPoint) {
        var methodName = joinPoint.getSignature().toShortString();
        var args = sanitizeArgs(joinPoint.getArgs());

        log.info("🎯 Entering controller method: {} with arguments: {}", methodName, args);
    }

    @Around("controllerMethods()")
    public Object logControllerExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        var startTime = Instant.now();
        var methodName = joinPoint.getSignature().toShortString();

        try {
            var result = joinPoint.proceed();
            var duration = Duration.between(startTime, Instant.now());

            log.info("✅ " + CONTROLLER_EXECUTION_MESSAGE, methodName, duration.toMillis());

            return result;
        } catch (Exception ex) {
            var duration = Duration.between(startTime, Instant.now());
            log.error("❌ Controller method: {} failed after {} ms with error: {}",
                    methodName, duration.toMillis(), ex.getMessage());
            throw ex;
        }
    }

    @Before("serviceMethods()")
    public void logServiceEntry(JoinPoint joinPoint) {
        var methodName = joinPoint.getSignature().toShortString();
        var args = sanitizeArgs(joinPoint.getArgs());

        log.debug("🔧 Entering service method: {} with arguments: {}", methodName, args);
    }

    @Around("serviceMethods()")
    public Object logServiceExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        var stopWatch = new StopWatch(joinPoint.getSignature().toShortString());
        stopWatch.start();

        try {
            var result = joinPoint.proceed();
            stopWatch.stop();

            log.debug("⚙️ " + SERVICE_EXECUTION_MESSAGE,
                    joinPoint.getSignature().toShortString(),
                    stopWatch.getTotalTimeMillis());

            return result;
        } catch (Exception ex) {
            stopWatch.stop();
            log.error("💥 Service method: {} failed after {} ms with error: {}",
                    joinPoint.getSignature().toShortString(),
                    stopWatch.getTotalTimeMillis(),
                    ex.getMessage());
            throw ex;
        }
    }

    @AfterThrowing(pointcut = "controllerMethods() || serviceMethods()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        var methodName = joinPoint.getSignature().toShortString();
        var exceptionType = exception.getClass().getSimpleName();
        var message = exception.getMessage();

        log.error("🚨 Exception in method: {} - Type: {} - Message: {}",
                methodName, exceptionType, message);

        log.debug("Stack trace for method: {}", methodName, exception);
    }

    private String sanitizeArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }

        return Arrays.stream(args)
            .map(this::sanitizeObject)
            .toList()
            .toString();
    }

    private Object sanitizeObject(Object obj) {
        if (obj == null) {
            return null;
        }

        String objStr = obj.toString();
        
        for (String field : SENSITIVE_FIELDS) {
            if (objStr.toLowerCase().contains(field.toLowerCase())) {
                objStr = objStr.replaceAll(
                    "(?i)(" + field + "[=:]\\s*)([^,\\s}]+)",
                    "$1***"
                );
            }
        }
        
        return objStr;
    }
}