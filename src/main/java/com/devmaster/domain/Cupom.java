package com.devmaster.domain;

import com.devmaster.domain.enums.TipoDesconto;
import com.devmaster.handler.APIException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cupons")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cupom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String codigo;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_desconto", nullable = false, length = 20)
    private TipoDesconto tipoDesconto;
    
    @Column(name = "valor_desconto", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorDesconto;
    
    @Column(name = "valor_minimo_pedido", precision = 10, scale = 2)
    private BigDecimal valorMinimoPedido = BigDecimal.ZERO;
    
    @Column(name = "desconto_maximo", precision = 10, scale = 2)
    private BigDecimal descontoMaximo;
    
    @Column(name = "limite_uso")
    private Integer limiteUso;
    
    @Column(name = "quantidade_usada", nullable = false)
    private Integer quantidadeUsada = 0;
    
    @Column(name = "valido_de", nullable = false)
    private LocalDateTime validoDe;
    
    @Column(name = "valido_ate", nullable = false)
    private LocalDateTime validoAte;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
    
    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
    }
    
    // Métodos de negócio
    
    public void atualizar(String descricao, BigDecimal valorDesconto, BigDecimal valorMinimoPedido, 
                         BigDecimal descontoMaximo, Integer limiteUso, 
                         LocalDateTime validoDe, LocalDateTime validoAte) {
        
        if (validoDe != null && validoAte != null && validoDe.isAfter(validoAte)) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Data de início deve ser anterior à data de fim");
        }
        
        if (descricao != null) this.descricao = descricao;
        if (valorDesconto != null) this.valorDesconto = valorDesconto;
        if (valorMinimoPedido != null) this.valorMinimoPedido = valorMinimoPedido;
        if (descontoMaximo != null) this.descontoMaximo = descontoMaximo;
        if (limiteUso != null) this.limiteUso = limiteUso;
        if (validoDe != null) this.validoDe = validoDe;
        if (validoAte != null) this.validoAte = validoAte;
        
        // Re-validar datas caso apenas uma tenha sido atualizada e gerado inconsistência
        if (this.validoDe.isAfter(this.validoAte)) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Data de início deve ser anterior à data de fim");
        }
    }

    public void ativar() {
        this.ativo = true;
    }
    
    public void desativar() {
        this.ativo = false;
    }
    
    public boolean isValido() {
        LocalDateTime agora = LocalDateTime.now();
        return ativo && 
               agora.isAfter(validoDe) && 
               agora.isBefore(validoAte) &&
               (limiteUso == null || quantidadeUsada < limiteUso);
    }
    
    public boolean podeSerUsado() {
        return isValido();
    }
    
    public void incrementarUso() {
        this.quantidadeUsada++;
    }
    
    public BigDecimal calcularDesconto(BigDecimal valorPedido) {
        if (!isValido()) {
            throw new IllegalStateException("Cupom inválido ou expirado");
        }
        
        if (valorPedido.compareTo(valorMinimoPedido) < 0) {
            throw new IllegalArgumentException(
                String.format("Valor mínimo do pedido não atingido. Mínimo: R$ %.2f", valorMinimoPedido)
            );
        }
        
        BigDecimal desconto;
        
        if (tipoDesconto == TipoDesconto.PERCENTUAL) {
            // Desconto percentual
            desconto = valorPedido.multiply(valorDesconto).divide(BigDecimal.valueOf(100));
            
            // Aplicar desconto máximo se definido
            if (descontoMaximo != null && desconto.compareTo(descontoMaximo) > 0) {
                desconto = descontoMaximo;
            }
        } else {
            // Desconto fixo
            desconto = valorDesconto;
            
            // Desconto não pode ser maior que o valor do pedido
            if (desconto.compareTo(valorPedido) > 0) {
                desconto = valorPedido;
            }
        }
        
        return desconto;
    }
    
    public boolean isExpirado() {
        return LocalDateTime.now().isAfter(validoAte);
    }
    
    public boolean atingiuLimiteUso() {
        return limiteUso != null && quantidadeUsada >= limiteUso;
    }
}
