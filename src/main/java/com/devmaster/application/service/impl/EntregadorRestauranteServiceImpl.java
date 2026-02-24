package com.devmaster.application.service.impl;

import com.devmaster.application.api.response.EntregadorResumoResponse;
import com.devmaster.application.service.EntregadorRestauranteService;
import com.devmaster.domain.Entregador;
import com.devmaster.domain.EntregadorRestaurante;
import com.devmaster.domain.Restaurante;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.EntregadorRepository;
import com.devmaster.infrastructure.repository.EntregadorRestauranteRepository;
import com.devmaster.infrastructure.repository.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EntregadorRestauranteServiceImpl implements EntregadorRestauranteService {
    
    private final EntregadorRestauranteRepository repository;
    private final EntregadorRepository entregadorRepository;
    private final RestauranteRepository restauranteRepository;
    
    @Override
    @Transactional
    public void vincular(Long entregadorId, Long restauranteId) {
        Entregador entregador = entregadorRepository.findById(entregadorId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Entregador não encontrado"));
        
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));
        
        if (repository.existsByEntregadorIdAndRestauranteIdAndAtivoTrue(entregadorId, restauranteId)) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Entregador já vinculado a este restaurante");
        }
        
        var vinculo = repository.findByEntregadorIdAndRestauranteId(entregadorId, restauranteId);
        
        if (vinculo.isPresent() && !vinculo.get().getAtivo()) {
            vinculo.get().reativar();
            repository.save(vinculo.get());
        } else {
            EntregadorRestaurante novoVinculo = EntregadorRestaurante.builder()
                .entregador(entregador)
                .restaurante(restaurante)
                .build();
            repository.save(novoVinculo);
        }
    }
    
    @Override
    @Transactional
    public void desvincular(Long entregadorId, Long restauranteId) {
        EntregadorRestaurante vinculo = repository.findByEntregadorIdAndRestauranteId(entregadorId, restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Vínculo não encontrado"));
        
        if (!vinculo.getAtivo()) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Vínculo já está inativo");
        }
        
        vinculo.desvincular();
        repository.save(vinculo);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EntregadorResumoResponse> listarEntregadoresPorRestaurante(Long restauranteId) {
        return repository.findByRestauranteIdAndAtivoTrue(restauranteId)
            .stream()
            .map(v -> EntregadorResumoResponse.from(v.getEntregador()))
            .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EntregadorResumoResponse> listarEntregadoresDisponiveisPorRestaurante(Long restauranteId) {
        return repository.findEntregadoresDisponiveisPorRestaurante(restauranteId)
            .stream()
            .map(v -> EntregadorResumoResponse.from(v.getEntregador()))
            .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public EntregadorResumoResponse buscarPorCpf(String cpf) {
        Entregador entregador = entregadorRepository.findByCpf(cpf)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Entregador não encontrado"));
        return EntregadorResumoResponse.from(entregador);
    }
}
