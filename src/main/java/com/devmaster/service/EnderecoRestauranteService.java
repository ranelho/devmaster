package com.devmaster.service;

import com.devmaster.domain.EnderecoRestaurante;

import java.util.List;

public interface EnderecoRestauranteService {

    List<EnderecoRestaurante> findAll();

    EnderecoRestaurante findById(Long id);

    List<EnderecoRestaurante> findAllByRestauranteId(Long restauranteId);

    void deletar(Long id);

}
