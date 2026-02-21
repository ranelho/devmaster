package com.devmaster.application.api.request;

import jakarta.validation.constraints.*;

/**
 * Request para criação de ImagemProduto.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record ImagemProdutoRequest(
    @NotBlank(message = "Nome do arquivo é obrigatório")
    @Size(max = 255, message = "Nome do arquivo deve ter no máximo 255 caracteres")
    String nomeArquivo,
    
    @NotBlank(message = "Tipo MIME é obrigatório")
    @Size(max = 100, message = "Tipo MIME deve ter no máximo 100 caracteres")
    String tipoMime,
    
    @NotNull(message = "Tamanho em bytes é obrigatório")
    @Min(value = 1, message = "Tamanho deve ser maior que zero")
    Long tamanhoBytes,
    
    @Min(value = 1, message = "Largura deve ser maior que zero")
    Integer largura,
    
    @Min(value = 1, message = "Altura deve ser maior que zero")
    Integer altura,
    
    String imagemBase64,
    
    @Size(max = 500, message = "URL do bucket deve ter no máximo 500 caracteres")
    String urlBucket,
    
    Boolean principal,
    
    @Min(value = 0, message = "Ordem de exibição deve ser maior ou igual a zero")
    Integer ordemExibicao
) {}
