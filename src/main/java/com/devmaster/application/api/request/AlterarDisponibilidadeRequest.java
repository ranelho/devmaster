package com.devmaster.application.api.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO de requisição para alterar disponibilidade do entregador.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlterarDisponibilidadeRequest {
    
    @NotNull(message = "Status de disponibilidade é obrigatório")
    private Boolean disponivel;
    
    @DecimalMin(value = "-90.0", message = "Latitude deve estar entre -90 e 90")
    @DecimalMax(value = "90.0", message = "Latitude deve estar entre -90 e 90")
    private BigDecimal latitude;
    
    @DecimalMin(value = "-180.0", message = "Longitude deve estar entre -180 e 180")
    @DecimalMax(value = "180.0", message = "Longitude deve estar entre -180 e 180")
    private BigDecimal longitude;
}
