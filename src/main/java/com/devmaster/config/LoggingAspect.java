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

/**
 * Aspect para logging automático e monitoramento de performance.
 * Utiliza recursos modernos do Java 25 e Spring Boot 4.
 * 
 * @author devmaster Team
 * @since 1.0.0
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private static final String CONTROLLER_EXECUTION_MESSAGE = "Controller method: {} executed in {} ms";
    private static final String SERVICE_EXECUTION_MESSAGE = "Service method: {} executed in {} ms";
    private static final String REPOSITORY_EXECUTION_MESSAGE = "Repository method: {} executed in {} ms";
    private static final String SLOW_QUERY_THRESHOLD_MS = "1000";

    /**
     * Pointcut for all methods in controller packages
     */
    @Pointcut("execution(* com.devmaster.controller..*(..))")
    public void controllerMethods() {}

    /**
     * Pointcut for all methods in service packages
     */
    @Pointcut("execution(* com.devmaster.service..*(..))")
    public void serviceMethods() {}

    /**
     * Pointcut for all methods in repository packages
     */
    @Pointcut("execution(* com.devmaster.repository..*(..))")
    public void repositoryMethods() {}

    /**
     * Log method entry for controllers with structured logging
     */
    @Before("controllerMethods()")
    public void logControllerEntry(JoinPoint joinPoint) {
        var methodName = joinPoint.getSignature().toShortString();
        var args = Arrays.toString(joinPoint.getArgs());
        
        log.info("🎯 Entering controller method: {} with arguments: {}", methodName, args);
    }

    /**
     * Log method execution time and result for controllers using modern Java features
     */
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

    /**
     * Log method entry for services with debug level
     */
    @Before("serviceMethods()")
    public void logServiceEntry(JoinPoint joinPoint) {
        var methodName = joinPoint.getSignature().toShortString();
        var args = Arrays.toString(joinPoint.getArgs());
        
        log.debug("🔧 Entering service method: {} with arguments: {}", methodName, args);
    }

    /**
     * Log method execution time for services using enhanced performance monitoring
     */
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

    /**
     * Log repository method execution time with slow query detection
     */
    @Around("repositoryMethods()")
    public Object logRepositoryExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        var startTime = Instant.now();
        var methodName = joinPoint.getSignature().toShortString();
        
        try {
            var result = joinPoint.proceed();
            var duration = Duration.between(startTime, Instant.now());
            var executionTimeMs = duration.toMillis();
            
            // Detect slow queries using modern comparison
            if (executionTimeMs > Long.parseLong(SLOW_QUERY_THRESHOLD_MS)) {
                log.warn("🐌 Slow repository method: {} executed in {} ms", methodName, executionTimeMs);
            } else {
                log.debug("🗄️ " + REPOSITORY_EXECUTION_MESSAGE, methodName, executionTimeMs);
            }
            
            return result;
        } catch (Exception ex) {
            var duration = Duration.between(startTime, Instant.now());
            log.error("💀 Repository method: {} failed after {} ms with error: {}", 
                    methodName, duration.toMillis(), ex.getMessage());
            throw ex;
        }
    }

    /**
     * Log exceptions for all methods with enhanced error information
     */
    @AfterThrowing(pointcut = "controllerMethods() || serviceMethods() || repositoryMethods()", 
                   throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        var methodName = joinPoint.getSignature().toShortString();
        var exceptionType = exception.getClass().getSimpleName();
        var message = exception.getMessage();
        
        log.error("🚨 Exception in method: {} - Type: {} - Message: {}", 
                methodName, exceptionType, message);
        
        // Log stack trace only for debug level to avoid log pollution
        log.debug("Stack trace for method: {}", methodName, exception);
    }
}