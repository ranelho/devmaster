package com.devmaster.application.api.request;

import com.devmaster.domain.enums.TipoIntervaloDesconto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados para criar/atualizar desconto")
public class DescontoRequest {
    
    @NotNull(message = "ID do produto é obrigatório")
    @Schema(description = "ID do produto", example = "1")
    private Long produtoId;
    
    @NotNull(message = "Percentual de desconto é obrigatório")
    @DecimalMin(value = "0.01", message = "Percentual deve ser maior que 0")
    @DecimalMax(value = "100.00", message = "Percentual deve ser no máximo 100")
    @Schema(description = "Percentual de desconto", example = "15.50")
    private BigDecimal percentualDesconto;
    
    @NotNull(message = "Tipo de intervalo é obrigatório")
    @Schema(description = "Tipo de intervalo (DIAS ou HORAS)", example = "DIAS")
    private TipoIntervaloDesconto tipoIntervalo;
    
    @NotNull(message = "Data/hora de início é obrigatória")
    @Future(message = "Data/hora de início deve ser futura")
    @Schema(description = "Data e hora de início do desconto", example = "2025-01-01T00:00:00")
    private LocalDateTime dataHoraInicio;
    
    @NotNull(message = "Data/hora de fim é obrigatória")
    @Future(message = "Data/hora de fim deve ser futura")
    @Schema(description = "Data e hora de fim do desconto", example = "2025-01-31T23:59:59")
    private LocalDateTime dataHoraFim;
    
    @Schema(description = "Desconto ativo", example = "true")
    private Boolean ativo = true;
}
