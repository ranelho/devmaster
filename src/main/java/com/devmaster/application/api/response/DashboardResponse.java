package com.devmaster.application.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    
    private DashboardMetricasResponse metricas;
    private List<ResumoStatusResponse> resumo;
    private List<PedidoResumoResponse> novos;
    private List<PedidoResumoResponse> preparo;
    private List<PedidoResumoResponse> prontos;
    private List<PedidoResumoResponse> entrega;
}
