package com.devmaster.service.impl;

import com.devmaster.application.api.request.RestauranteRequest;
import com.devmaster.domain.Restaurante;
import com.devmaster.handler.APIException;
import com.devmaster.infra.RestauranteRepository;
import com.devmaster.service.RestauranteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    @Transactional
    public Restaurante criar(RestauranteRequest request) {
        if (restauranteRepository.existsByCnpj(request.cnpj())) {
            throw APIException.build(HttpStatus.NOT_FOUND, "CNPJ já cadastrado");
        }
        if (restauranteRepository.existsBySlug(request.slug())) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Slug já cadastrado");
        }
        return this.restauranteRepository.save(new Restaurante(request));
    }

    @Override
    public Restaurante alternarAtivo(Long id) {
        Restaurante restaurante = this.findById(id);
        restaurante.alternarAtivo();
        return this.restauranteRepository.save(restaurante);
    }

    @Override
    @Transactional
    public Restaurante atualizar(Long id, RestauranteRequest request) {
        final var restaurante = this.findById(id);
        restaurante.update(request);
        return this.restauranteRepository.save(restaurante);
    }

}
