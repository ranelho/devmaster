package com.devmaster.service;

import com.devmaster.domain.Restaurante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RestauranteService {

    List<Restaurante> findAll();

    Restaurante findById(Long id);

    Page<Restaurante> findAllPageable(Pageable pageable);

    Restaurante ativar(Long id);

    Restaurante inativar(Long id);
}
