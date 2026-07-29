package com.devmaster.service.impl;

import com.devmaster.application.api.request.CategoriaRequest;
import com.devmaster.domain.Categoria;
import com.devmaster.handler.APIException;
import com.devmaster.infra.CategoriaRepository;
import com.devmaster.service.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<Categoria> findAllAtivoTrueByRestauranteId(Long id) {
        return categoriaRepository.findAllByRestauranteId(id);
    }

    @Override
    public List<Categoria> findAllAtivoFalseByRestauranteId(Long id) {
        return List.of();
    }

    @Override
    public Categoria criar(CategoriaRequest request) {
        return this.categoriaRepository.save(new Categoria(request));
    }

    @Override
    public Categoria atualizar(Long id, CategoriaRequest request) {
        final var categoria = this.findById(id);
        categoria.update(request);
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria alternarAtivo(Long id) {
        final var categoria = this.findById(id);
        categoria.alternar();
        return categoriaRepository.save(categoria);
    }

    private Categoria findById(Long id) {
        return categoriaRepository.findById(id).orElseThrow(
                () -> APIException.build(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }
}
