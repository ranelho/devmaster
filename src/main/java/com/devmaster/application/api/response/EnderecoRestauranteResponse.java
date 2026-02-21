package com.devmaster.application.api.response;

import com.devmaster.domain.EnderecoRestaurante;

import java.math.BigDecimal;

/**
 * DTO de resposta com dados do endereço do restaurante.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record EnderecoRestauranteResponse(
    Long id,
    Long restauranteId,
    String logradouro,
    String numero,
    String complemento,
    String bairro,
    String cidade,
    String estado,
    String cep,
    BigDecimal latitude,
    BigDecimal longitude
) {
    public static EnderecoRestauranteResponse from(EnderecoRestaurante endereco) {
        return new EnderecoRestauranteResponse(
            endereco.getId(),
            endereco.getRestaurante().getId(),
            endereco.getLogradouro(),
            endereco.getNumero(),
            endereco.getComplemento(),
            endereco.getBairro(),
            endereco.getCidade(),
            endereco.getEstado(),
            endereco.getCep(),
            endereco.getLatitude(),
            endereco.getLongitude()
        );
    }
}
