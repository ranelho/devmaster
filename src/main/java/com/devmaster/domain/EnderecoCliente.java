package com.devmaster.domain;

import com.devmaster.application.api.request.EnderecoClienteRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "enderecos_cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "bairro", nullable = false, length = 100)
    private String bairro;

    @Column(name = "cep", nullable = false, length = 10)
    private String cep;

    @Column(name = "cidade", nullable = false, length = 100)
    private String cidade;

    @Column(name = "complemento", nullable = false, length = 255)
    private String complemento;

    @Column(name = "estado", nullable = false, length = 2)
    private String estado;

    @Column(name = "logradouro", nullable = false, length = 255)
    private String logradouro;

    @Column(name = "numero", nullable = false, length = 20)
    private String numero;

    @Column(name = "rotulo", length = 50)
    private String rotulo;

    @Column(name = "padrao", nullable = false)
    private Boolean padrao;

    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    //@Column
    //private Cliente cliente;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;


    public EnderecoCliente(EnderecoClienteRequest enderecoClienteRequest) {
        this.bairro = enderecoClienteRequest.bairro();
        this.cep = enderecoClienteRequest.cep();
        this.cidade = enderecoClienteRequest.cidade();
        this.estado = enderecoClienteRequest.estado();
        this.logradouro = enderecoClienteRequest.logradouro();
        this.numero = enderecoClienteRequest.numero();
        this.complemento = enderecoClienteRequest.complemento();
        this.rotulo = enderecoClienteRequest.rotulo();
        this.latitude = enderecoClienteRequest.latitude();
        this.longitude = enderecoClienteRequest.longitude();
    }

    public void update(EnderecoClienteRequest request) {
        this.bairro = request.bairro();
        this.cep = request.cep();
        this.cidade = request.cidade();
        this.estado = request.estado();
        this.logradouro = request.logradouro();
        this.numero = request.numero();
        this.complemento = request.complemento();
        this.rotulo = request.rotulo();
        this.latitude = request.latitude();
        this.longitude = request.longitude();
        this.atualizadoEm = LocalDateTime.now();
    }

}
