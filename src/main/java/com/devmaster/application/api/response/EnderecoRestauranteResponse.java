package com.devmaster.application.api.response;

import com.devmaster.domain.EnderecoRestaurante;

import java.math.BigDecimal;
import java.util.List;

public record EnderecoRestauranteResponse (

        Long id,
        String bairro,
        String cep,
        String cidade,
        String complemento,
        String estado,
        BigDecimal latitude,
        String logradouro,
        BigDecimal longitude,
        String numero,
        Long restauranteId
) {
    public EnderecoRestauranteResponse(EnderecoRestaurante enderecoRestaurante) {
        this(
                enderecoRestaurante.getId(),
                enderecoRestaurante.getBairro(),
                enderecoRestaurante.getCep(),
                enderecoRestaurante.getCidade(),
                enderecoRestaurante.getComplemento(),
                enderecoRestaurante.getEstado(),
                enderecoRestaurante.getLatitude(),
                enderecoRestaurante.getLogradouro(),
                enderecoRestaurante.getLongitude(),
                enderecoRestaurante.getNumero(),
                enderecoRestaurante.getRestaurante().getId()
        );
    }

    public static List<EnderecoRestauranteResponse> convert(List<EnderecoRestaurante> list) {
        return list.stream().map(EnderecoRestauranteResponse::new).toList();
    }
}
