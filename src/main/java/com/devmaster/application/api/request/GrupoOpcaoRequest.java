package com.devmaster.application.api.request;

import jakarta.validation.constraints.*;

/**
 * Request para criação de GrupoOpcao.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record GrupoOpcaoRequest(
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    String nome,
    
    @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres")
    String descricao,
    
    @Min(value = 0, message = "Mínimo de seleções deve ser maior ou igual a zero")
    Integer minimoSelecoes,
    
    @Min(value = 1, message = "Máximo de seleções deve ser maior ou igual a um")
    Integer maximoSelecoes,
    
    Boolean obrigatorio,
    
    @Min(value = 0, message = "Ordem de exibição deve ser maior ou igual a zero")
    Integer ordemExibicao
) {}
