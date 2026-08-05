package com.devmaster.service;

import com.devmaster.application.api.request.ProdutoRequest;
import com.devmaster.domain.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProdutoService {

    Page<Produto> findAllByCategoriaId(Long id, Pageable pageable);

    Produto findByid(Long id);

    Produto criar(ProdutoRequest request);

    Produto atualizar(Long id, ProdutoRequest request);

    Produto alternarDisponibilidade(Long id);

}
