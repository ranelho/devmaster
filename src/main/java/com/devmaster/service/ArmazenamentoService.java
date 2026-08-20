package com.devmaster.service;

import com.devmaster.application.api.response.ImagemProdutoResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


public interface ArmazenamentoService {

    public ImagemProdutoResponse uploadImagem(MultipartFile file, Long produtoId);

}
