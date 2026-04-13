package com.devmaster.service;

import com.devmaster.application.api.request.RestauranteRequest;
import com.devmaster.domain.Restaurante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RestauranteService {

    List<Restaurante> findAll();

    Restaurante findById(Long id);

    Page<Restaurante> findAllPageable(Pageable pageable);

    Restaurante criar(RestauranteRequest request);

    Restaurante ativar(Long id);

    Restaurante inativar(Long id);

    Restaurante atualizar(Long id, RestauranteRequest request);
}
