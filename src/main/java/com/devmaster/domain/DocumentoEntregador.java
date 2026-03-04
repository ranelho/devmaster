package com.devmaster.domain;

import com.devmaster.domain.enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "documentos_entregador", indexes = {
    @Index(name = "idx_documentos_entregador_entregador_id", columnList = "entregador_id"),
    @Index(name = "idx_documentos_entregador_tipo", columnList = "tipo_documento"),
    @Index(name = "idx_documentos_entregador_verificado", columnList = "verificado")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoEntregador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entregador_id", nullable = false)
    private Entregador entregador;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false, length = 50)
    private TipoDocumento tipoDocumento;
    
    @Column(name = "numero_documento", length = 100)
    private String numeroDocumento;
    
    @Column(name = "url_arquivo", nullable = false, length = 500)
    private String urlArquivo;
    
    @Column(name = "data_validade")
    private LocalDate dataValidade;
    
    @Column(name = "verificado", nullable = false)
    @Builder.Default
    private Boolean verificado = false;
    
    @Column(name = "verificado_em")
    private LocalDateTime verificadoEm;
    
    @Column(name = "verificado_por")
    private UUID verificadoPor;
    
    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
    
    public void verificar(UUID verificadoPor) {
        this.verificado = true;
        this.verificadoEm = LocalDateTime.now();
        this.verificadoPor = verificadoPor;
    }
    
    public void removerVerificacao() {
        this.verificado = false;
        this.verificadoEm = null;
        this.verificadoPor = null;
    }
    
    public boolean isVencido() {
        if (dataValidade == null) {
            return false;
        }
        return LocalDate.now().isAfter(dataValidade);
    }
    
    /**
     * Verifica se o documento está próximo do vencimento (30 dias).
     * 
     * @return true se próximo do vencimento, false caso contrário
     */
    public boolean isProximoVencimento() {
        if (dataValidade == null) {
            return false;
        }
        LocalDate dataLimite = LocalDate.now().plusDays(30);
        return dataValidade.isBefore(dataLimite) && !isVencido();
    }
}
