package com.devmaster.application.api.response;

import com.devmaster.domain.enums.TipoIntervaloDesconto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados do desconto")
public class DescontoResponse {
    
    @Schema(description = "ID do desconto", example = "1")
    private Long id;
    
    @Schema(description = "ID do produto", example = "1")
    private Long produtoId;
    
    @Schema(description = "Nome do produto", example = "Pizza Margherita")
    private String nomeProduto;
    
    @Schema(description = "Percentual de desconto", example = "15.50")
    private BigDecimal percentualDesconto;
    
    @Schema(description = "Tipo de intervalo", example = "DIAS")
    private TipoIntervaloDesconto tipoIntervalo;
    
    @Schema(description = "Data e hora de início", example = "2025-01-01T00:00:00")
    private LocalDateTime dataHoraInicio;
    
    @Schema(description = "Data e hora de fim", example = "2025-01-31T23:59:59")
    private LocalDateTime dataHoraFim;
    
    @Schema(description = "Desconto ativo", example = "true")
    private Boolean ativo;
    
    @Schema(description = "Desconto vigente no momento", example = "true")
    private Boolean vigente;
    
    @Schema(description = "Data de criação", example = "2024-12-26T10:30:00")
    private LocalDateTime criadoEm;
    
    @Schema(description = "Data de atualização", example = "2024-12-26T10:30:00")
    private LocalDateTime atualizadoEm;
}
