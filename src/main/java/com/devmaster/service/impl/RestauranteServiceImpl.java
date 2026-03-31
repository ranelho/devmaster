package com.devmaster.service.impl;

import com.devmaster.domain.Restaurante;
import com.devmaster.handler.APIException;
import com.devmaster.infra.RestauranteRepository;
import com.devmaster.service.RestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestauranteServiceImpl implements RestauranteService {

    private final RestauranteRepository restauranteRepository;

    @Override
    public List<Restaurante> findAll() {
        return this.restauranteRepository.findAll();
    }

    @Override
    public Restaurante findById(Long id) {
        return this.restauranteRepository.findById(id)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));
    }

    @Override
    public Page<Restaurante> findAllPageable(Pageable pageable) {
        return this.restauranteRepository.findAll(pageable);
    }

    @Override
    public Restaurante ativar(Long id) {
        Restaurante restaurante = this.findById(id);
        restaurante.ativoInativo(true);
        return this.restauranteRepository.save(restaurante);
    }

    @Override
    public Restaurante inativar(Long id) {
        Restaurante restaurante = this.findById(id);
        restaurante.ativoInativo(false);
        return this.restauranteRepository.save(restaurante);
    }

}
