package com.devmaster.domain;

import com.devmaster.domain.enums.StatusPagamento;
import com.devmaster.domain.enums.StatusPedido;
import com.devmaster.handler.APIException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_pedido", unique = true, nullable = false)
    private String numeroPedido;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "endereco_entrega_id", nullable = false)
    private EnderecoCliente enderecoEntrega;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_pagamento_id", nullable = false)
    private TipoPagamento tipoPagamento;
    
    @Column(name = "restaurante_id", insertable = false, updatable = false)
    private Long restauranteId;
    
    @Column(name = "cliente_nome")
    private String clienteNome;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private StatusPedido status;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento", nullable = false, length = 50)
    private StatusPagamento statusPagamento;
    
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "taxa_entrega", precision = 10, scale = 2)
    private BigDecimal taxaEntrega;
    
    @Column(name = "desconto", precision = 10, scale = 2)
    private BigDecimal desconto;
    
    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;
    
    @Column(name = "valor_troco", precision = 10, scale = 2)
    private BigDecimal valorTroco;
    
    @Column(columnDefinition = "TEXT")
    private String observacoes;
    
    @Column(name = "motivo_cancelamento", columnDefinition = "TEXT")
    private String motivoCancelamento;
    
    @Column(name = "previsao_entrega")
    private LocalDateTime previsaoEntrega;
    
    @Column(name = "data_pedido", nullable = false)
    private LocalDateTime dataPedido;
    
    @Column(name = "criado_em", nullable = false)
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
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        dataPedido = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
        if (cliente != null) {
            clienteNome = cliente.getNomeCompleto();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
    
    // Métodos de negócio
    
    public BigDecimal getTotal() {
        return valorTotal;
    }
    
    public void setTotal(BigDecimal total) {
        this.valorTotal = total;
    }
    
    public void confirmar() {
        if (status != StatusPedido.AGUARDANDO_CONFIRMACAO) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Pedido não pode ser confirmado neste status");
        }
        this.status = StatusPedido.CONFIRMADO;
        this.confirmadoEm = LocalDateTime.now();
    }
    
    public void iniciarPreparo() {
        if (status != StatusPedido.CONFIRMADO) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Pedido não pode iniciar preparo neste status");
        }
        this.status = StatusPedido.EM_PREPARO;
        this.preparandoEm = LocalDateTime.now();
    }
    
    public void marcarComoPronto() {
        if (status != StatusPedido.EM_PREPARO) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Pedido não pode ser marcado como pronto neste status");
        }
        this.status = StatusPedido.PRONTO;
        this.prontoEm = LocalDateTime.now();
    }
    
    public void despachar() {
        if (status != StatusPedido.PRONTO) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Pedido não pode ser despachado neste status");
        }
        this.status = StatusPedido.EM_ENTREGA;
        this.despachadoEm = LocalDateTime.now();
    }
    
    public void entregar() {
        if (status != StatusPedido.EM_ENTREGA) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Pedido não pode ser entregue neste status");
        }
        this.status = StatusPedido.ENTREGUE;
        this.entregueEm = LocalDateTime.now();
    }
    
    public boolean podeSerCancelado() {
        return status == StatusPedido.AGUARDANDO_CONFIRMACAO || 
               status == StatusPedido.CONFIRMADO || 
               status == StatusPedido.EM_PREPARO;
    }
    
    public void cancelar(String motivo) {
        if (!podeSerCancelado()) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Pedido não pode ser cancelado neste status");
        }
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
}
