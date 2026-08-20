package com.devmaster.service.impl;

import com.devmaster.application.api.response.ImagemProdutoResponse;
import com.devmaster.domain.ImagemProduto;
import com.devmaster.infra.ImagemProdutoRepository;
import com.devmaster.service.ImagemProdutoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

@Service
@AllArgsConstructor
public class ImagemProdutoServiceImpl implements ImagemProdutoService {

    private final ImagemProdutoRepository imagemProdutoRepository;

    @Override
    public ImagemProdutoResponse criar(MultipartFile arquivo, Long produtoId) {
        try  {
            BufferedImage image = ImageIO.read(arquivo.getInputStream());
            final var imagemProduto = new ImagemProduto(
                    produtoId,
                    arquivo.getName(),
                    arquivo.getContentType(),
                    arquivo.getSize(),
                    image.getWidth(),
                    image.getHeight(),
                    Arrays.toString(Base64.getEncoder().encode(arquivo.getBytes()))
            );

            return new ImagemProdutoResponse(imagemProdutoRepository.save(imagemProduto));
        } catch (IOException e) {
            throw new RuntimeException(
                    "Falhou na leitura do arquivo '%s' do request".formatted(arquivo.getName()), e);
        }

    }
}
