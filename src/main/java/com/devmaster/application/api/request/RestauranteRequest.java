package com.devmaster.application.api.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * DTO de requisição para criar restaurante.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record RestauranteRequest(
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    String nome,
    
    @NotBlank(message = "Slug é obrigatório")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug deve conter apenas letras minúsculas, números e hífens")
    @Size(max = 255, message = "Slug deve ter no máximo 255 caracteres")
    String slug,
    
    @Size(max = 5000, message = "Descrição deve ter no máximo 5000 caracteres")
    String descricao,
    
    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    String telefone,
    
    @Email(message = "Email inválido")
    String email,
    
    @Pattern(regexp = "^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$", message = "CNPJ inválido. Formato: 00.000.000/0000-00")
    String cnpj,
    
    String logoUrl,
    
    String bannerUrl,
    
    @DecimalMin(value = "0.0", message = "Taxa de entrega deve ser maior ou igual a 0")
    BigDecimal taxaEntrega,
    
    @DecimalMin(value = "0.0", message = "Valor mínimo do pedido deve ser maior ou igual a 0")
    BigDecimal valorMinimoPedido,
    
    @Min(value = 0, message = "Tempo médio de entrega deve ser maior ou igual a 0")
    Integer tempoMedioEntrega,
    
    @Valid
    EnderecoRestauranteRequest endereco
) {}
