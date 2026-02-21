package com.devmaster.application.api.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Request para atualização de Produto.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record AtualizarProdutoRequest(
    Long categoriaId,
    
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    String nome,
    
    @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres")
    String descricao,
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que zero")
    BigDecimal preco,
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Preço promocional deve ser maior que zero")
    BigDecimal precoPromocional,
    
    @Min(value = 0, message = "Tempo de preparo deve ser maior ou igual a zero")
    Integer tempoPreparo,
    
    @Min(value = 0, message = "Ordem de exibição deve ser maior ou igual a zero")
    Integer ordemExibicao
) {}
