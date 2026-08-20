package com.devmaster.application.api;

import com.devmaster.application.api.request.ProdutoRequest;
import com.devmaster.application.api.response.ImagemProdutoResponse;
import com.devmaster.application.api.response.ProdutoResponse;
import com.devmaster.service.ArmazenamentoService;
import com.devmaster.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequiredArgsConstructor
public class ProdutoRestController implements ProdutoApi {

    private final ProdutoService produtoService;
    private final ArmazenamentoService armazenamentoService;

    @Override
    public ResponseEntity<Page<ProdutoResponse>> findAllByCategoriaId(Long categoriaId, Integer page, Integer size) {
        final var pageable = PageRequest.of(page, size);
        final var produtos = this.produtoService
                .findAllByCategoriaId(categoriaId, pageable)
                .map(ProdutoResponse::new);
        return ResponseEntity.ok(produtos);
    }

    @Override
    public ResponseEntity<ProdutoResponse> findById(Long id) {
        final var response = new ProdutoResponse(this.produtoService.findByid(id));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ProdutoResponse> criar(ProdutoRequest request) {
        final var response = new ProdutoResponse(this.produtoService.criar(request));
        final var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Override
    public ResponseEntity<ImagemProdutoResponse> uploadImage(MultipartFile file, Long produtoId) {
        return ResponseEntity.ok(this.armazenamentoService.uploadImagem(file, produtoId));
    }

    @Override
    public ResponseEntity<ProdutoResponse> atualizar(Long id, ProdutoRequest request) {
        final var response = new ProdutoResponse(this.produtoService.atualizar(id, request));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ProdutoResponse> alternarDisponibilidade(Long id) {
        final var response = new ProdutoResponse(this.produtoService.alternarDisponibilidade(id));
        return ResponseEntity.ok(response);
    }
}
