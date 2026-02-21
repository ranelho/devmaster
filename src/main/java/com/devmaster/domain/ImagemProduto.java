package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade ImagemProduto.
 * Suporta armazenamento em base64 ou URL de bucket.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "imagens_produto")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImagemProduto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    
    @Column(name = "nome_arquivo", nullable = false)
    private String nomeArquivo;
    
    @Column(name = "tipo_mime", nullable = false, length = 100)
    private String tipoMime;
    
    @Column(name = "tamanho_bytes", nullable = false)
    private Long tamanhoBytes;
    
    private Integer largura;
    
    private Integer altura;
    
    @Column(name = "imagem_base64", columnDefinition = "TEXT")
    private String imagemBase64;
    
    @Column(name = "url_bucket", length = 500)
    private String urlBucket;
    
    @Column(nullable = false)
    private Boolean principal = false;
    
    @Column(name = "ordem_exibicao")
    private Integer ordemExibicao = 0;
    
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
    
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
    
    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        atualizadoEm = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }
    
    // Métodos de negócio
    
    public void definirComoPrincipal() {
        this.principal = true;
    }
    
    public void removerPrincipal() {
        this.principal = false;
    }
    
    public boolean temUrlBucket() {
        return urlBucket != null && !urlBucket.isBlank();
    }
    
    public boolean temBase64() {
        return imagemBase64 != null && !imagemBase64.isBlank();
    }
}
