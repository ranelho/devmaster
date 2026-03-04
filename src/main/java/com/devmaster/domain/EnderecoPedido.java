package com.devmaster.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EnderecoPedido {

    @Column(name = "endereco_logradouro", nullable = false)
    private String logradouro;

    @Column(name = "endereco_numero", nullable = false)
    private String numero;

    @Column(name = "endereco_complemento")
    private String complemento;

    @Column(name = "endereco_bairro", nullable = false)
    private String bairro;

    @Column(name = "endereco_cidade", nullable = false)
    private String cidade;

    @Column(name = "endereco_estado", nullable = false)
    private String estado;

    @Column(name = "endereco_cep", nullable = false)
    private String cep;

    @Column(name = "endereco_latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "endereco_longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    public static EnderecoPedido from(EnderecoCliente enderecoCliente) {
        if (enderecoCliente == null) return null;
        
        return EnderecoPedido.builder()
                .logradouro(enderecoCliente.getLogradouro())
                .numero(enderecoCliente.getNumero())
                .complemento(enderecoCliente.getComplemento())
                .bairro(enderecoCliente.getBairro())
                .cidade(enderecoCliente.getCidade())
                .estado(enderecoCliente.getEstado())
                .cep(enderecoCliente.getCep())
                .latitude(enderecoCliente.getLatitude())
                .longitude(enderecoCliente.getLongitude())
                .build();
    }
}
