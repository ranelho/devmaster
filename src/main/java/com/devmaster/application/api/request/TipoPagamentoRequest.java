package com.devmaster.application.api.request;

import jakarta.validation.constraints.*;

/**
 * Request para criação de TipoPagamento.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record TipoPagamentoRequest(
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    String nome,
    
    @NotBlank(message = "Código é obrigatório")
    @Size(max = 30, message = "Código deve ter no máximo 30 caracteres")
    String codigo,
    
    @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres")
    String descricao,
    
    @Size(max = 500, message = "URL do ícone deve ter no máximo 500 caracteres")
    String iconeUrl,
    
    Boolean requerTroco,
    
    @Min(value = 0, message = "Ordem de exibição deve ser maior ou igual a zero")
    Integer ordemExibicao
) {}
