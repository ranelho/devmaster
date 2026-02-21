package com.devmaster.domain;

import com.devmaster.domain.enums.StatusPagamento;
import com.devmaster.domain.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade Pedido.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "pedidos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_pedido", nullable = false, unique = true, length = 20)
    private String numeroPedido;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;
    
    @ManyToOne
    @JoinColumn(name = "endereco_entrega_id", nullable = false)
    private EnderecoCliente enderecoEntrega;
    
    @ManyToOne
    @JoinColumn(name = "tipo_pagamento_id", nullable = false)
    private TipoPagamento tipoPagamento;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusPedido status = StatusPedido.PENDENTE;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento", nullable = false, length = 30)
    private StatusPagamento statusPagamento = StatusPagamento.PENDENTE;
    
    @Column(name = "valor_troco", precision = 10, scale = 2)
    private BigDecimal valorTroco;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "taxa_entrega", nullable = false, precision = 10, scale = 2)
    private BigDecimal taxaEntrega;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal desconto = BigDecimal.ZERO;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    
    @Column(columnDefinition = "TEXT")
    private String observacoes;
    
    @Column(name = "previsao_entrega")
    private LocalDateTime previsaoEntrega;
    
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
    
    @Column(name = "confirmado_em")
    private LocalDateTime confirmadoEm;
    
    @Column(name = "preparando_em")
    private LocalDateTime preparandoEm;
    
    @Column(name = "pronto_em")
    private LocalDateTime prontoEm;
    
    @Column(name = "despachado_em")
    private LocalDateTime despachadoEm;
    
    @Column(name = "entregue_em")
    private LocalDateTime entregueEm;
    
    @Column(name = "cancelado_em")
    private LocalDateTime canceladoEm;
    
    @Column(name = "motivo_cancelamento", columnDefinition = "TEXT")
    private String motivoCancelamento;
    
    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
    }
    
    // Métodos de negócio
    
    public void confirmar() {
        this.status = StatusPedido.CONFIRMADO;
        this.confirmadoEm = LocalDateTime.now();
    }
    
    public void iniciarPreparo() {
        this.status = StatusPedido.PREPARANDO;
        this.preparandoEm = LocalDateTime.now();
    }
    
    public void marcarComoPronto() {
        this.status = StatusPedido.PRONTO;
        this.prontoEm = LocalDateTime.now();
    }
    
    public void despachar() {
        this.status = StatusPedido.DESPACHADO;
        this.despachadoEm = LocalDateTime.now();
    }
    
    public void entregar() {
        this.status = StatusPedido.ENTREGUE;
        this.entregueEm = LocalDateTime.now();
    }
    
    public void cancelar(String motivo) {
        this.status = StatusPedido.CANCELADO;
        this.canceladoEm = LocalDateTime.now();
        this.motivoCancelamento = motivo;
    }
    
    public void aprovarPagamento() {
        this.statusPagamento = StatusPagamento.APROVADO;
    }
    
    public void recusarPagamento() {
        this.statusPagamento = StatusPagamento.RECUSADO;
    }
    
    public boolean podeSerCancelado() {
        return status == StatusPedido.PENDENTE || status == StatusPedido.CONFIRMADO;
    }
    
    public boolean estaCancelado() {
        return status == StatusPedido.CANCELADO;
    }
    
    public boolean estaEntregue() {
        return status == StatusPedido.ENTREGUE;
    }
}
