package com.devmaster.application.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO de resposta com estatísticas do entregador.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstatisticasEntregadorResponse {
    
    private Long entregadorId;
    private String nomeCompleto;
    private Integer totalEntregas;
    private BigDecimal avaliacaoMedia;
    private BigDecimal taxaSucesso;
    private Integer tempoMedioEntregaMinutos;
    private Integer entregasUltimos30Dias;
    private Integer totalMudancasDisponibilidade;
    private Integer documentosVerificados;
    private Integer documentosPendentes;
    private Integer documentosVencidos;
}
