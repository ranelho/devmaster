package com.devmaster.application.api.response;

import com.devmaster.domain.Pedido;
import com.devmaster.domain.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResumoResponse {
    
    private Long id;
    private String numeroPedido;
    private String clienteNome;
    private String restauranteNome;
    private BigDecimal valorTotal;
    private StatusPedido status;
    private String statusDescricao;
    private LocalDateTime dataPedido;
    private LocalDateTime criadoEm;
    private LocalDateTime previsaoEntrega;
    
    public static PedidoResumoResponse from(Pedido pedido) {
        return PedidoResumoResponse.builder()
            .id(pedido.getId())
            .numeroPedido(pedido.getNumeroPedido())
            .clienteNome(pedido.getClienteNome())
            .restauranteNome(pedido.getRestaurante() != null ? pedido.getRestaurante().getNome() : null)
            .valorTotal(pedido.getValorTotal())
            .status(pedido.getStatus())
            .statusDescricao(pedido.getStatus().getDescricao())
            .dataPedido(pedido.getDataPedido())
            .criadoEm(pedido.getCriadoEm())
            .previsaoEntrega(pedido.getPrevisaoEntrega())
            .build();
    }
}
