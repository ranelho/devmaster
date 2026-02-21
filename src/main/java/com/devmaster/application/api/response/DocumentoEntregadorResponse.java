package com.devmaster.application.api.response;

import com.devmaster.domain.DocumentoEntregador;
import com.devmaster.domain.enums.TipoDocumento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de resposta com dados do documento do entregador.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record DocumentoEntregadorResponse(
    Long id,
    Long entregadorId,
    TipoDocumento tipoDocumento,
    String numeroDocumento,
    String urlArquivo,
    LocalDate dataValidade,
    Boolean verificado,
    LocalDateTime verificadoEm,
    UUID verificadoPor,
    LocalDateTime criadoEm,
    Boolean vencido,
    Boolean proximoVencimento
) {
    public static DocumentoEntregadorResponse from(DocumentoEntregador documento) {
        return new DocumentoEntregadorResponse(
            documento.getId(),
            documento.getEntregador().getId(),
            documento.getTipoDocumento(),
            documento.getNumeroDocumento(),
            documento.getUrlArquivo(),
            documento.getDataValidade(),
            documento.getVerificado(),
            documento.getVerificadoEm(),
            documento.getVerificadoPor(),
            documento.getCriadoEm(),
            documento.isVencido(),
            documento.isProximoVencimento()
        );
    }
}
