package com.devmaster.service;

import com.devmaster.application.api.response.ImagemProdutoResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImagemProdutoService {

    ImagemProdutoResponse criar(MultipartFile arquivo, Long produtoId);
}
