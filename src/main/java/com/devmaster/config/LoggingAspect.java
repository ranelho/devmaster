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

    private static final String CONTROLLER_EXECUTION_MESSAGE = "Método do controller: {} finalizado em {} ms";
    private static final String SERVICE_EXECUTION_MESSAGE = "Método do service: {} finalizado em {} ms";
    private static final Set<String> SENSITIVE_FIELDS = Set.of(
        "password", "senha", "cpf", "cnpj", "cnh", "token", 
        "accessToken", "refreshToken", "authorization"
    );

    @Pointcut("execution(* com.devmaster..api..*(..))")
    public void controllerMethods() {
    }

    @Pointcut("within(@org.springframework.stereotype.Service *) || execution(* com.devmaster..service..*(..))")
    public void serviceMethods() {
    }

    @Before("controllerMethods()")
    public void logControllerEntry(JoinPoint joinPoint) {
        var methodName = joinPoint.getSignature().toShortString();
        var args = sanitizeArgs(joinPoint.getArgs());

        log.info("🎯 Iniciando método do controller: {} com argumentos: {}", methodName, args);
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
            log.error("❌ Método do controller: {} falhou após {} ms com erro: {}",
                    methodName, duration.toMillis(), ex.getMessage());
            throw ex;
        }
    }

    @Before("serviceMethods()")
    public void logServiceEntry(JoinPoint joinPoint) {
        var methodName = joinPoint.getSignature().toShortString();
        var args = sanitizeArgs(joinPoint.getArgs());

        log.info("🔧 Iniciando método do service: {} com argumentos: {}", methodName, args);
    }

    @Around("serviceMethods()")
    public Object logServiceExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        var stopWatch = new StopWatch(joinPoint.getSignature().toShortString());
        stopWatch.start();

        try {
            var result = joinPoint.proceed();
            stopWatch.stop();

            log.info("⚙️ " + SERVICE_EXECUTION_MESSAGE,
                    joinPoint.getSignature().toShortString(),
                    stopWatch.getTotalTimeMillis());

            return result;
        } catch (Exception ex) {
            stopWatch.stop();
            log.error("💥 Método do service: {} falhou após {} ms com erro: {}",
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

        if (exception instanceof NullPointerException) {
            log.error("🚨 Exceção no método: {} - Tipo: {} - Mensagem: {} - Trecho: {}",
                    methodName, exceptionType, message, formatStackSnippet(exception, 3));
            return;
        }

        log.error("🚨 Exceção no método: {} - Tipo: {} - Mensagem: {}",
                methodName, exceptionType, message);

        log.debug("Stack trace do método: {}", methodName, exception);
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

    private String formatStackSnippet(Throwable exception, int maxFrames) {
        var stackTrace = exception.getStackTrace();
        if (stackTrace == null || stackTrace.length == 0) {
            return "sem stack trace";
        }

        var limit = Math.min(maxFrames, stackTrace.length);
        var builder = new StringBuilder();

        for (int i = 0; i < limit; i++) {
            if (i > 0) {
                builder.append(" | ");
            }
            builder.append(stackTrace[i]);
        }

        if (stackTrace.length > limit) {
            builder.append(" | ...");
        }

        return builder.toString();
    }
}
