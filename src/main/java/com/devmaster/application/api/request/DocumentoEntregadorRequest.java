package com.devmaster.application.api.request;

import com.devmaster.domain.enums.TipoDocumento;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO de requisição para adicionar documento do entregador.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoEntregadorRequest {
    
    @NotNull(message = "Tipo de documento é obrigatório")
    private TipoDocumento tipoDocumento;
    
    @Size(max = 100, message = "Número do documento não pode exceder 100 caracteres")
    private String numeroDocumento;
    
    @NotBlank(message = "URL do arquivo é obrigatória")
    @Size(max = 500, message = "URL do arquivo não pode exceder 500 caracteres")
    private String urlArquivo;
    
    @Future(message = "Data de validade deve ser futura")
    private LocalDate dataValidade;
}
