package com.devmaster.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 🛡️ Global Exception Handler para tratamento centralizado de exceções
 * 
 * Este handler captura e trata todas as exceções da aplicação de forma consistente,
 * fornecendo respostas padronizadas e logs estruturados para facilitar o debug
 * e melhorar a experiência do usuário.
 * 
 * Funcionalidades:
 * - Tratamento de exceções de validação
 * - Exceções de banco de dados
 * - Exceções HTTP (404, 405, 415, etc.)
 * - Exceções de negócio customizadas
 * - Exceções genéricas com fallback
 * - Logs estruturados com contexto
 * - Respostas padronizadas em JSON
 * 
 * @author DevMaster
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 📝 Trata erros de validação de campos (@Valid)
     * 
     * Captura erros quando a validação de Bean Validation falha,
     * retornando detalhes específicos de cada campo inválido.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        log.warn("❌ Erro de validação na requisição: {} {}", 
            request.getMethod(), request.getRequestURI());
        
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
            
            log.debug("   📋 Campo '{}': {}", fieldName, errorMessage);
        });
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Validation Failed")
            .message("Dados inválidos fornecidos")
            .path(request.getRequestURI())
            .method(request.getMethod())
            .details(fieldErrors)
            .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 🔍 Trata violações de constraint (@NotNull, @Size, etc.)
     * 
     * Captura violações de constraints de validação, especialmente
     * em parâmetros de métodos e validações programáticas.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest request) {
        
        log.warn("⚠️ Violação de constraint na requisição: {} {}", 
            request.getMethod(), request.getRequestURI());
        
        Map<String, String> violations = ex.getConstraintViolations()
            .stream()
            .collect(Collectors.toMap(
                violation -> violation.getPropertyPath().toString(),
                ConstraintViolation::getMessage
            ));
        
        violations.forEach((field, message) -> 
            log.debug("   🚫 Constraint '{}': {}", field, message));
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Constraint Violation")
            .message("Violação de regras de validação")
            .path(request.getRequestURI())
            .method(request.getMethod())
            .details(violations)
            .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 📄 Trata erros de parsing de JSON/XML
     * 
     * Captura erros quando o corpo da requisição não pode ser
     * deserializado para o objeto esperado.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        
        log.warn("📄 Erro de parsing do corpo da requisição: {} {}", 
            request.getMethod(), request.getRequestURI());
        log.debug("   💥 Detalhes: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Malformed Request")
            .message("Formato do corpo da requisição é inválido")
            .path(request.getRequestURI())
            .method(request.getMethod())
            .details(Map.of("cause", "JSON/XML malformado ou tipo incompatível"))
            .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 🔗 Trata parâmetros obrigatórios ausentes
     * 
     * Captura quando parâmetros marcados como @RequestParam(required=true)
     * não são fornecidos na requisição.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        
        log.warn("🔗 Parâmetro obrigatório ausente: {} {} - Parâmetro: '{}'", 
            request.getMethod(), request.getRequestURI(), ex.getParameterName());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Missing Parameter")
            .message("Parâmetro obrigatório não fornecido")
            .path(request.getRequestURI())
            .method(request.getMethod())
            .details(Map.of(
                "parameter", ex.getParameterName(),
                "type", ex.getParameterType(),
                "description", "Este parâmetro é obrigatório para a operação"
            ))
            .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 🔄 Trata erros de conversão de tipo
     * 
     * Captura quando um parâmetro não pode ser convertido para o tipo esperado
     * (ex: string para número, formato de data inválido).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        
        log.warn("🔄 Erro de conversão de tipo: {} {} - Parâmetro: '{}', Valor: '{}'", 
            request.getMethod(), request.getRequestURI(), ex.getName(), ex.getValue());
        
        String expectedType = ex.getRequiredType() != null ? 
            ex.getRequiredType().getSimpleName() : "unknown";
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Type Mismatch")
            .message("Tipo de dados inválido para o parâmetro")
            .path(request.getRequestURI())
            .method(request.getMethod())
            .details(Map.of(
                "parameter", ex.getName(),
                "providedValue", String.valueOf(ex.getValue()),
                "expectedType", expectedType,
                "description", "O valor fornecido não pode ser convertido para o tipo esperado"
            ))
            .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 🚫 Trata método HTTP não suportado (405)
     * 
     * Captura quando um endpoint é chamado com método HTTP incorreto
     * (ex: POST em endpoint que só aceita GET).
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        
        log.warn("🚫 Método HTTP não suportado: {} {} - Métodos aceitos: {}", 
            request.getMethod(), request.getRequestURI(), ex.getSupportedMethods());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.METHOD_NOT_ALLOWED.value())
            .error("Method Not Allowed")
            .message("Método HTTP não suportado para este endpoint")
            .path(request.getRequestURI())
            .method(request.getMethod())
            .details(Map.of(
                "supportedMethods", ex.getSupportedMethods() != null ? 
                    String.join(", ", ex.getSupportedMethods()) : "N/A",
                "description", "Use um dos métodos HTTP suportados"
            ))
            .build();
        
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    /**
     * 📎 Trata tipo de mídia não suportado (415)
     * 
     * Captura quando o Content-Type da requisição não é suportado
     * (ex: enviar XML para endpoint que só aceita JSON).
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        
        log.warn("📎 Tipo de mídia não suportado: {} {} - Content-Type: {}", 
            request.getMethod(), request.getRequestURI(), ex.getContentType());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
            .error("Unsupported Media Type")
            .message("Tipo de conteúdo não suportado")
            .path(request.getRequestURI())
            .method(request.getMethod())
            .details(Map.of(
                "providedType", ex.getContentType() != null ? 
                    ex.getContentType().toString() : "N/A",
                "supportedTypes", ex.getSupportedMediaTypes().toString(),
                "description", "Use um dos tipos de conteúdo suportados"
            ))
            .build();
        
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
    }

    /**
     * 🔍 Trata endpoint não encontrado (404)
     * 
     * Captura quando uma URL não corresponde a nenhum endpoint mapeado.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(
            NoHandlerFoundException ex, HttpServletRequest request) {
        
        log.warn("🔍 Endpoint não encontrado: {} {}", 
            request.getMethod(), request.getRequestURI());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error("Not Found")
            .message("Endpoint não encontrado")
            .path(request.getRequestURI())
            .method(request.getMethod())
            .details(Map.of(
                "description", "O endpoint solicitado não existe",
                "suggestion", "Verifique a URL e o método HTTP"
            ))
            .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * 🗄️ Trata erros de banco de dados
     * 
     * Captura exceções relacionadas ao acesso a dados,
     * incluindo violações de integridade e problemas de conexão.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        
        log.error("🗄️ Violação de integridade de dados: {} {}", 
            request.getMethod(), request.getRequestURI());
        log.debug("   💥 Detalhes: {}", ex.getMessage());
        
        // Analisa o tipo de violação para fornecer mensagem mais específica
        String message = "Violação de regra de integridade do banco de dados";
        Map<String, String> details = new HashMap<>();
        
        if (ex.getMessage() != null) {
            String errorMsg = ex.getMessage().toLowerCase();
            if (errorMsg.contains("unique") || errorMsg.contains("duplicate")) {
                message = "Registro duplicado - valor já existe";
                details.put("type", "DUPLICATE_ENTRY");
            } else if (errorMsg.contains("foreign key") || errorMsg.contains("constraint")) {
                message = "Violação de chave estrangeira - referência inválida";
                details.put("type", "FOREIGN_KEY_VIOLATION");
            } else if (errorMsg.contains("not null")) {
                message = "Campo obrigatório não pode ser nulo";
                details.put("type", "NOT_NULL_VIOLATION");
            }
        }
        
        details.put("description", "Verifique os dados e tente novamente");
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.CONFLICT.value())
            .error("Data Integrity Violation")
            .message(message)
            .path(request.getRequestURI())
            .method(request.getMethod())
            .details(details)
            .build();
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * 🗄️ Trata erros gerais de acesso a dados
     * 
     * Captura outras exceções relacionadas ao banco de dados
     * que não são violações de integridade.
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(
            DataAccessException ex, HttpServletRequest request) {
        
        log.error("🗄️ Erro de acesso a dados: {} {}", 
            request.getMethod(), request.getRequestURI());
        log.debug("   💥 Detalhes: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error("Database Error")
            .message("Erro interno do banco de dados")
            .path(request.getRequestURI())
            .method(request.getMethod())
            .details(Map.of(
                "type", "DATABASE_ACCESS_ERROR",
                "description", "Erro temporário no acesso aos dados"
            ))
            .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * 💼 Trata exceções de regras de negócio
     * 
     * Captura exceções customizadas da aplicação que representam
     * violações de regras de negócio ou cenários específicos.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {
        
        log.warn("💼 Exceção de negócio: {} {} - {}", 
            request.getMethod(), request.getRequestURI(), ex.getMessage());
        
        Map<String, String> details = new HashMap<>();
        if (ex.getErrorCode() != null) {
            details.put("errorCode", ex.getErrorCode());
        }
        if (ex.getDetails() != null) {
            details.put("details", ex.getDetails().toString());
        }
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(ex.getStatus().value())
            .error("Business Rule Violation")
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .method(request.getMethod())
            .details(details.isEmpty() ? null : details)
            .build();
        
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    /**
     * 💥 Trata exceções genéricas não capturadas
     * 
     * Fallback para qualquer exceção não tratada especificamente,
     * garantindo que sempre haja uma resposta consistente.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        log.error("💥 Erro interno não tratado: {} {}", 
            request.getMethod(), request.getRequestURI());
        log.error("   🔍 Exceção: {}", ex.getClass().getSimpleName());
        log.debug("   📋 Stack trace:", ex);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error("Internal Server Error")
            .message("Erro interno do servidor")
            .path(request.getRequestURI())
            .method(request.getMethod())
            .details(Map.of(
                "type", "INTERNAL_ERROR",
                "description", "Erro inesperado no processamento da requisição"
            ))
            .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}