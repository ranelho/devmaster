package com.devmaster.application.api.response;

import com.devmaster.domain.Restaurante;

import java.math.BigDecimal;
import java.util.List;

public record RestauranteResponse(
        Long id,
        String cnpj,
        String nome,
        String descricao,
        String bannerUrl,
        Boolean aberto,
        Boolean ativo,
        BigDecimal avaliacao,
        String logoUrl,
        String slug,
        BigDecimal taxaEntrega,
        String telefone,
        String email,
        Long empresaId
){
    public RestauranteResponse(Restaurante restaurante) {
        this(
                restaurante.getId(),
                restaurante.getCnpj(),
                restaurante.getNome(),
                restaurante.getDescricao(),
                restaurante.getBannerUrl(),
                restaurante.getAberto(),
                restaurante.getAtivo(),
                restaurante.getAvaliacao(),
                restaurante.getLogoUrl(),
                restaurante.getSlug(),
                restaurante.getTaxaEntrega(),
                restaurante.getTelefone(),
                restaurante.getEmail(),
                restaurante.getEmpresaId());
    }

    public static List<RestauranteResponse> convert(List<Restaurante> list) {
        return list.stream().map(RestauranteResponse::new).toList();
    }
}
