package com.devmaster.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 💼 Exceção base para regras de negócio
 * 
 * Exceção customizada para representar violações de regras de negócio
 * da aplicação. Permite definir status HTTP específico e mensagens
 * detalhadas para diferentes cenários de erro.
 * 
 * Exemplos de uso:
 * - Usuário não encontrado
 * - Operação não permitida
 * - Limite de recursos excedido
 * - Validações de negócio específicas
 * 
 * @author DevMaster
 * @since 1.0.0
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 🔢 Status HTTP a ser retornado
     */
    private final HttpStatus status;

    /**
     * 🏷️ Código de erro específico (opcional)
     */
    private final String errorCode;

    /**
     * 📋 Detalhes adicionais (opcional)
     */
    private final Object details;

    /**
     * Construtor básico com mensagem
     */
    public BusinessException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = null;
        this.details = null;
    }

    /**
     * Construtor com mensagem e status HTTP
     */
    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.errorCode = null;
        this.details = null;
    }

    /**
     * Construtor completo com todos os parâmetros
     */
    public BusinessException(String message, HttpStatus status, String errorCode, Object details) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
        this.details = details;
    }

    /**
     * Construtor com causa raiz
     */
    public BusinessException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.errorCode = null;
        this.details = null;
    }

    // Métodos de conveniência para cenários comuns

    /**
     * 🔍 Recurso não encontrado (404)
     */
    public static BusinessException notFound(String resource) {
        return new BusinessException(
            String.format("%s não encontrado", resource),
            HttpStatus.NOT_FOUND,
            "RESOURCE_NOT_FOUND",
            null
        );
    }

    /**
     * 🚫 Operação não permitida (403)
     */
    public static BusinessException forbidden(String operation) {
        return new BusinessException(
            String.format("Operação '%s' não permitida", operation),
            HttpStatus.FORBIDDEN,
            "OPERATION_FORBIDDEN",
            null
        );
    }

    /**
     * ⚠️ Conflito de dados (409)
     */
    public static BusinessException conflict(String message) {
        return new BusinessException(
            message,
            HttpStatus.CONFLICT,
            "DATA_CONFLICT",
            null
        );
    }

    /**
     * 📝 Dados inválidos (400)
     */
    public static BusinessException invalidData(String field, String reason) {
        return new BusinessException(
            String.format("Campo '%s' inválido: %s", field, reason),
            HttpStatus.BAD_REQUEST,
            "INVALID_DATA",
            field
        );
    }
}