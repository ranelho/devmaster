package com.devmaster.application.api.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record PedidoRequest(
    @NotNull(message = "Cliente é obrigatório")
    Long clienteId,
    
    @NotNull(message = "Restaurante é obrigatório")
    Long restauranteId,
    
    @NotNull(message = "Endereço de entrega é obrigatório")
    Long enderecoEntregaId,
    
    @NotNull(message = "Tipo de pagamento é obrigatório")
    Long tipoPagamentoId,
    
    @DecimalMin(value = "0.0", message = "Valor do troco deve ser maior ou igual a zero")
    BigDecimal valorTroco,
    
    @Size(max = 2000, message = "Observações devem ter no máximo 2000 caracteres")
    String observacoes,
    
    String codigoCupom,
    
    @NotEmpty(message = "Pedido deve ter pelo menos um item")
    @Valid
    List<ItemPedidoRequest> itens
) {}
