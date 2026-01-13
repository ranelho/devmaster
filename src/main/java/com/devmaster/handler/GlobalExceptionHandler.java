package com.devmaster.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> details = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            details.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        StandardError error = StandardError.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .error("Validation Error")
                .message("Erros de validação encontrados")
                .timestamp(LocalDateTime.now())
                .uuid(UUID.randomUUID().toString())
                .details(details)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<StandardError> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> details = new HashMap<>();

        ex.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            details.put(propertyPath, message);
        });

        StandardError error = StandardError.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .error("Validation Error")
                .message("Erros de validação encontrados")
                .timestamp(LocalDateTime.now())
                .uuid(UUID.randomUUID().toString())
                .details(details)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        StandardError error = StandardError.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .error("Malformed JSON request")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .uuid(UUID.randomUUID().toString())
                .details(null)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler({BusinessException.class, IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<StandardError> handleBusinessException(RuntimeException ex) {
        StandardError error = StandardError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .uuid(UUID.randomUUID().toString())
                .details(null)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> handleResourceNotFoundException(ResourceNotFoundException ex) {
        StandardError error = StandardError.builder()
                .status(HttpStatus.NOT_FOUND.name())
                .code(HttpStatus.NOT_FOUND.value())
                .error("Resource Not Found")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .uuid(UUID.randomUUID().toString())
                .details(null)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DuplicidadeException.class)
    public ResponseEntity<StandardError> handleDuplicidadeException(DuplicidadeException ex) {
        StandardError error = StandardError.builder()
                .status(HttpStatus.CONFLICT.name())
                .code(HttpStatus.CONFLICT.value())
                .error("Resource Conflict")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .uuid(UUID.randomUUID().toString())
                .details(null)
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

}
