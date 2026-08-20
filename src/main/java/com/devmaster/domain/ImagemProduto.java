package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "imagens_produto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImagemProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "produto_id", referencedColumnName = "id")
    private Produto produto;

    @Column(name = "nome_arquivo")
    private String nomeArquivo;

    @Column(name = "tipo_mime")
    private String tipoMime;

    @Column(name = "tamanho_byte")
    private Long tamanhoByte;

    @Column(name = "largura")
    private Integer largura;

    @Column(name = "altura")
    private Integer altura;

    @Column(name = "imagem_base64")
    private String imagemBase64;

    @Column(name = "url_bucket")
    private String urlBucket;

    @Column(name = "principal")
    private Boolean principal;

    @Column(name = "ordem_exibicao")
    private Integer ordemExibicao;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criandoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    public ImagemProduto(Long produtoid, String nomeArquivo, String tipoMime, Long tamanhoByte, Integer largura, Integer altura, String imagemBase64) {
        this.produto = new Produto();
        produto.setId(produtoid);
        this.nomeArquivo = nomeArquivo;
        this.tipoMime = tipoMime;
        this.tamanhoByte = tamanhoByte;
        this.largura = largura;
        this.altura = altura;
        this.imagemBase64 = imagemBase64;
        this.principal = true;
        this.ordemExibicao = 0;
        this.criandoEm = LocalDateTime.now();
    }
}
