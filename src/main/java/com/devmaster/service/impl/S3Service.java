package com.devmaster.service.impl;

import com.devmaster.application.api.response.ImagemProdutoResponse;
import com.devmaster.service.ArmazenamentoService;
import com.devmaster.service.ImagemProdutoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class S3Service implements ArmazenamentoService {

    private final S3Client s3Client;
    private final String bucketName;
    private final ImagemProdutoService imagemProdutoService;

    @Override
    public ImagemProdutoResponse uploadImagem(MultipartFile arquivo, Long produtoId) {
        validarArquivo(arquivo);

        final var nomeArquivo = Objects.requireNonNull(arquivo.getOriginalFilename());
        final var extensao = nomeArquivo.substring(nomeArquivo.lastIndexOf('.') + 1);
        final var novoNome = UUID.randomUUID() + "." + extensao;
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(novoNome)
                    .contentType(arquivo.getContentType())
                    .contentLength(arquivo.getSize())
                    .build();

            s3Client.putObject(request,
                    RequestBody.fromInputStream(
                            arquivo.getInputStream(),
                            arquivo.getSize()));
            return imagemProdutoService.criar(arquivo, produtoId);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Falhou na leitura do arquivo '%s' do request".formatted(nomeArquivo), e);
        } catch (S3Exception e) {
            throw new RuntimeException(
                    "R2 rejeitou o upload de '%s': %s".formatted(nomeArquivo, e.awsErrorDetails().errorMessage()), e);
        } catch (SdkException e) {
            throw new RuntimeException(
                    "Erro enquanto fazia upload: '%s'".formatted(nomeArquivo), e);
        }
    }

    private void validarArquivo(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new RuntimeException("Não é possivel fazer upload de arquivo vazio");
        }
        if (!StringUtils.hasText(arquivo.getOriginalFilename())) {
            throw new RuntimeException("Arquivo precisa de um nome");
        }
        if (!arquivo.getContentType().contains("image")) {
            throw new RuntimeException("Só png, jpeg e webp são permitidos");
        }
    }
}
