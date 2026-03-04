package com.devmaster.application.api.response;

import com.devmaster.domain.enums.TipoIntervaloDesconto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Dados do desconto")
public record DescontoResponse(
    @Schema(description = "ID do desconto", example = "1")
    Long id,
    
    @Schema(description = "ID do produto", example = "1")
    Long produtoId,
    
    @Schema(description = "Nome do produto", example = "Pizza Margherita")
    String nomeProduto,
    
    @Schema(description = "Percentual de desconto", example = "15.50")
    BigDecimal percentualDesconto,
    
    @Schema(description = "Tipo de intervalo", example = "DIAS")
    TipoIntervaloDesconto tipoIntervalo,
    
    @Schema(description = "Data e hora de início", example = "2025-01-01T00:00:00")
    LocalDateTime dataHoraInicio,
    
    @Schema(description = "Data e hora de fim", example = "2025-01-31T23:59:59")
    LocalDateTime dataHoraFim,
    
    @Schema(description = "Desconto ativo", example = "true")
    Boolean ativo,
    
    @Schema(description = "Desconto vigente no momento", example = "true")
    Boolean vigente,
    
    @Schema(description = "Data de criação", example = "2024-12-26T10:30:00")
    LocalDateTime criadoEm,
    
    @Schema(description = "Data de atualização", example = "2024-12-26T10:30:00")
    LocalDateTime atualizadoEm
) {}
