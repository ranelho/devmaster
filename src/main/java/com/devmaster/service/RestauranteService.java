package com.devmaster.service;

import com.devmaster.application.api.request.RestauranteRequest;
import com.devmaster.domain.Restaurante;

import java.util.List;

public interface RestauranteService {

    List<Restaurante> findAll();

    Restaurante findById(Long id);

    Restaurante criar(RestauranteRequest request);

    Restaurante alternarAtivo(Long id);

    Restaurante atualizar(Long id, RestauranteRequest request);
}
