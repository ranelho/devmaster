package com.devmaster.service;

import com.devmaster.application.api.request.CategoriaRequest;
import com.devmaster.domain.Categoria;

import java.util.List;

public interface CategoriaService {

    List<Categoria> findAllAtivoTrueByRestauranteId(Long id);

    List<Categoria> findAllAtivoFalseByRestauranteId(Long id);

    Categoria criar(CategoriaRequest request);

    Categoria atualizar(Long id, CategoriaRequest request);

    Categoria alternarAtivo(Long id);


}
