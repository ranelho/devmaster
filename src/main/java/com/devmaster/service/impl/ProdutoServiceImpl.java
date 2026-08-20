package com.devmaster.service.impl;

import com.devmaster.application.api.request.ProdutoRequest;
import com.devmaster.domain.Produto;
import com.devmaster.handler.APIException;
import com.devmaster.infra.CategoriaRepository;
import com.devmaster.infra.ProdutoRepository;
import com.devmaster.service.ProdutoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRespository;

    @Override
    public Page<Produto> findAllByCategoriaId(Long id, Pageable pageable) {
        if(!categoriaRespository.existsById(id)) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Categoria não encontrada");
        }
        return produtoRepository.findAllByCategoriaIdOrderByNomeAsc(id, pageable);
    }

    @Override
    public Produto findByid(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    @Override
    public Produto criar(ProdutoRequest request) {
        if (request.precoPromocional().compareTo(request.preco()) > 0) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Preço promocional não pode ser menor que o preço normal");
        }

        return this.produtoRepository.save(new Produto(request));
    }

    @Override
    public Produto atualizar(Long id, ProdutoRequest request) {
        if (request.precoPromocional().compareTo(request.preco()) > 0) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Preço promocional não pode ser menor que o preço normal");
        }
        final var produto = this.findByid(id);
        produto.update(request);
        return this.produtoRepository.save(produto);
    }

    @Override
    public Produto alternarDisponibilidade(Long id) {
        final var produto = this.findByid(id);
        produto.alternarDisponibilidade();
        return this.produtoRepository.save(produto);
    }
}
