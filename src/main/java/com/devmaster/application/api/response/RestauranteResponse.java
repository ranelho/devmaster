package com.devmaster.application.api.response;

import com.devmaster.domain.Restaurante;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
                restaurante.getEmpresaId()
        );
    }

    public static List<RestauranteResponse> convert(List<Restaurante> list) {
        return list.stream().map(RestauranteResponse::new).collect(Collectors.toList());
    }

    public static Page<RestauranteResponse> convertPageable(Page<Restaurante> list) {
        return list.map(RestauranteResponse::new);
    }
}
