package com.devmaster.application.api;

import com.devmaster.application.api.response.ImagemProdutoResponse;
import org.springframework.http.ResponseEntity;


public class ImagemProdutoRestController implements ImagemProdutoApi {

    @Override
    public ResponseEntity<ImagemProdutoResponse> alterarPrincipal(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        return null;
    }
}
