package com.devmaster.util;

import com.devmaster.handler.APIException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Set;

/**
 * Utilitário para manipulação de imagens.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Slf4j
public class ImagemUtil {
    
    private static final Set<String> TIPOS_MIME_PERMITIDOS = Set.of(
        "image/jpeg",
        "image/jpg",
        "image/png",
        "image/gif",
        "image/webp"
    );
    
    private static final long TAMANHO_MAXIMO_BYTES = 5 * 1024 * 1024; // 5MB
    
    /**
     * Converte MultipartFile para Base64.
     */
    public static String converterParaBase64(MultipartFile arquivo) {
        try {
            validarArquivo(arquivo);
            byte[] bytes = arquivo.getBytes();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            log.error("Erro ao converter imagem para base64", e);
            throw APIException.build(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Erro ao processar imagem", e);
        }
    }
    
    /**
     * Converte Base64 para array de bytes.
     */
    public static byte[] converterBase64ParaBytes(String base64) {
        try {
            // Remove prefixo data:image se existir
            String base64Limpo = base64;
            if (base64.contains(",")) {
                base64Limpo = base64.split(",")[1];
            }
            return Base64.getDecoder().decode(base64Limpo);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao decodificar base64", e);
            throw APIException.build(HttpStatus.BAD_REQUEST, 
                "Base64 inválido", e);
        }
    }
    
    /**
     * Obtém dimensões da imagem (largura e altura).
     */
    public static DimensoesImagem obterDimensoes(MultipartFile arquivo) {
        try {
            byte[] bytes = arquivo.getBytes();
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            BufferedImage image = ImageIO.read(bis);
            
            if (image == null) {
                throw APIException.build(HttpStatus.BAD_REQUEST, 
                    "Não foi possível ler a imagem");
            }
            
            return new DimensoesImagem(image.getWidth(), image.getHeight());
        } catch (IOException e) {
            log.error("Erro ao obter dimensões da imagem", e);
            throw APIException.build(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Erro ao processar imagem", e);
        }
    }
    
    /**
     * Obtém dimensões da imagem a partir de Base64.
     */
    public static DimensoesImagem obterDimensoesBase64(String base64) {
        try {
            byte[] bytes = converterBase64ParaBytes(base64);
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            BufferedImage image = ImageIO.read(bis);
            
            if (image == null) {
                throw APIException.build(HttpStatus.BAD_REQUEST, 
                    "Não foi possível ler a imagem");
            }
            
            return new DimensoesImagem(image.getWidth(), image.getHeight());
        } catch (IOException e) {
            log.error("Erro ao obter dimensões da imagem", e);
            throw APIException.build(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Erro ao processar imagem", e);
        }
    }
    
    /**
     * Valida arquivo de imagem.
     */
    public static void validarArquivo(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw APIException.build(HttpStatus.BAD_REQUEST, 
                "Arquivo de imagem é obrigatório");
        }
        
        String contentType = arquivo.getContentType();
        if (contentType == null || !TIPOS_MIME_PERMITIDOS.contains(contentType.toLowerCase())) {
            throw APIException.build(HttpStatus.BAD_REQUEST, 
                "Tipo de arquivo não permitido. Permitidos: JPEG, PNG, GIF, WEBP");
        }
        
        if (arquivo.getSize() > TAMANHO_MAXIMO_BYTES) {
            throw APIException.build(HttpStatus.BAD_REQUEST, 
                "Arquivo muito grande. Tamanho máximo: 5MB");
        }
    }
    
    /**
     * Gera nome único para arquivo.
     */
    public static String gerarNomeUnico(String nomeOriginal) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String extensao = obterExtensao(nomeOriginal);
        return timestamp + "_" + nomeOriginal.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
    
    /**
     * Obtém extensão do arquivo.
     */
    public static String obterExtensao(String nomeArquivo) {
        if (nomeArquivo == null || !nomeArquivo.contains(".")) {
            return "";
        }
        return nomeArquivo.substring(nomeArquivo.lastIndexOf("."));
    }
    
    /**
     * Record para dimensões da imagem.
     */
    public record DimensoesImagem(int largura, int altura) {}
}
