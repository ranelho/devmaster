package com.devmaster.service;

import com.devmaster.application.api.request.EnderecoRestauranteRequest;
import com.devmaster.domain.EnderecoRestaurante;

import java.util.List;

public interface EnderecoRestauranteService {

    List<EnderecoRestaurante> findAll();

    EnderecoRestaurante findById(Long id);

    List<EnderecoRestaurante> findAllByRestauranteId(Long restauranteId);

    EnderecoRestaurante criar(EnderecoRestauranteRequest request);

    EnderecoRestaurante atualizar(Long id, EnderecoRestauranteRequest request);

    void deletar(Long id);

}
