package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que representa um endereço de entrega do cliente.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "enderecos_cliente", indexes = {
    @Index(name = "idx_enderecos_cliente_cliente_id", columnList = "cliente_id"),
    @Index(name = "idx_enderecos_cliente_cliente_padrao", columnList = "cliente_id, padrao")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoCliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @Column(name = "rotulo", length = 50)
    private String rotulo;
    
    @Column(name = "logradouro", nullable = false, length = 255)
    private String logradouro;
    
    @Column(name = "numero", nullable = false, length = 20)
    private String numero;
    
    @Column(name = "complemento", length = 255)
    private String complemento;
    
    @Column(name = "bairro", nullable = false, length = 100)
    private String bairro;
    
    @Column(name = "cidade", nullable = false, length = 100)
    private String cidade;
    
    @Column(name = "estado", nullable = false, length = 2)
    private String estado;
    
    @Column(name = "cep", nullable = false, length = 10)
    private String cep;
    
    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;
    
    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;
    
    @Column(name = "padrao", nullable = false)
    @Builder.Default
    private Boolean padrao = false;
    
    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
    
    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
    
    /**
     * Define este endereço como padrão.
     */
    public void definirComoPadrao() {
        this.padrao = true;
    }
    
    /**
     * Remove a marcação de endereço padrão.
     */
    public void removerPadrao() {
        this.padrao = false;
    }
    
    /**
     * Verifica se o endereço possui coordenadas geográficas.
     * 
     * @return true se possui latitude e longitude, false caso contrário
     */
    public boolean temCoordenadas() {
        return latitude != null && longitude != null;
    }
}
