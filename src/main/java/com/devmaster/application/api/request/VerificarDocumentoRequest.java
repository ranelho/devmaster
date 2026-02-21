package com.devmaster.application.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de requisição para verificar documento do entregador.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificarDocumentoRequest {
    
    @NotNull(message = "Status de verificação é obrigatório")
    private Boolean verificado;
    
    @Size(max = 500, message = "Observações não podem exceder 500 caracteres")
    private String observacoes;
}
