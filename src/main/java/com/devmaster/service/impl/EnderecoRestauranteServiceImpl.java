package com.devmaster.service.impl;

import com.devmaster.application.api.request.EnderecoRestauranteRequest;
import com.devmaster.domain.EnderecoRestaurante;
import com.devmaster.handler.APIException;
import com.devmaster.infra.EnderecoRestauranteRepository;
import com.devmaster.infra.RestauranteRepository;
import com.devmaster.service.EnderecoRestauranteService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EnderecoRestauranteServiceImpl implements EnderecoRestauranteService {

    private final EnderecoRestauranteRepository enderecoRestauranteRepository;

    private final RestauranteRepository restauranteRepository;

    @Override
    public List<EnderecoRestaurante> findAll() {
        return enderecoRestauranteRepository.findAll();
    }

    @Override
    public EnderecoRestaurante findById(Long id) {
        return enderecoRestauranteRepository.findById(id)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Endereço do Restaurante com id " + id + " não encontrado"));
    }

    @Override
    public List<EnderecoRestaurante> findAllByRestauranteId(Long restauranteId) {
        if (!this.restauranteRepository.existsById(restauranteId)) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Não existe um restaurante com id " + restauranteId);
        }
        return this.enderecoRestauranteRepository.findAllByRestauranteId(restauranteId);
    }

    @Override
    @Transactional
    public EnderecoRestaurante criar(EnderecoRestauranteRequest request) {
        if(!this.restauranteRepository.existsById(request.restauranteId())) {
            throw APIException.build(HttpStatus.NOT_FOUND, "O restaurante não existe");
        }
        return this.enderecoRestauranteRepository.save(new EnderecoRestaurante(request));
    }

    @Override
    @Transactional
    public EnderecoRestaurante atualizar(Long id, EnderecoRestauranteRequest request) {
        final var enderecoRestaurante = this.findById(id);
        enderecoRestaurante.update(request);
        return this.enderecoRestauranteRepository.save(enderecoRestaurante);
    }

    @Override
    public void deletar(Long id) {
        if(!this.enderecoRestauranteRepository.existsById(id)) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Endereço de Restaurante com id: " + id + " não encontrado");
        }
        this.enderecoRestauranteRepository.deleteById(id);
    }

}
