package com.devmaster.application.api.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * DTO de requisição para adicionar endereço do cliente.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record EnderecoClienteRequest(
    
    @Size(max = 50, message = "Rótulo não pode exceder 50 caracteres")
    String rotulo,
    
    @NotBlank(message = "Logradouro é obrigatório")
    @Size(max = 255, message = "Logradouro não pode exceder 255 caracteres")
    String logradouro,
    
    @NotBlank(message = "Número é obrigatório")
    @Size(max = 20, message = "Número não pode exceder 20 caracteres")
    String numero,
    
    @Size(max = 255, message = "Complemento não pode exceder 255 caracteres")
    String complemento,
    
    @NotBlank(message = "Bairro é obrigatório")
    @Size(max = 100, message = "Bairro não pode exceder 100 caracteres")
    String bairro,
    
    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 100, message = "Cidade não pode exceder 100 caracteres")
    String cidade,
    
    @NotBlank(message = "Estado é obrigatório")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Estado deve ter 2 letras maiúsculas (ex: SP)")
    String estado,
    
    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "CEP deve estar no formato XXXXX-XXX")
    String cep,
    
    @DecimalMin(value = "-90.0", message = "Latitude deve estar entre -90 e 90")
    @DecimalMax(value = "90.0", message = "Latitude deve estar entre -90 e 90")
    BigDecimal latitude,
    
    @DecimalMin(value = "-180.0", message = "Longitude deve estar entre -180 e 180")
    @DecimalMax(value = "180.0", message = "Longitude deve estar entre -180 e 180")
    BigDecimal longitude,
    
    Boolean padrao
) {}
