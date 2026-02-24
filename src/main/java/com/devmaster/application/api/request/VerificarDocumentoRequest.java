package com.devmaster.application.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de requisição para verificar documento do entregador.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record VerificarDocumentoRequest(
    @NotNull(message = "Status de verificação é obrigatório")
    Boolean verificado,
    
    @Size(max = 500, message = "Observações não podem exceder 500 caracteres")
    String observacoes
) {}
