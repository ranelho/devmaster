package com.devmaster.application.api;

import com.devmaster.application.api.response.ImagemProdutoResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Imagem do Produto", description = "API de gerenciamento de imagens dos produtos")
@RequestMapping("/public/v1/imagens")
public interface ImagemProdutoApi {

    @PatchMapping("/{id}/principal")
    ResponseEntity<ImagemProdutoResponse> alterarPrincipal(@PathVariable Long id);

    @DeleteMapping("{/id}")
    ResponseEntity<?> delete(@PathVariable Long id);

}
