package com.devmaster.application.api.response;

import com.devmaster.domain.EnderecoCliente;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de resposta com dados do endereço do cliente.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record EnderecoClienteResponse(
    Long id,
    Long clienteId,
    String rotulo,
    String logradouro,
    String numero,
    String complemento,
    String bairro,
    String cidade,
    String estado,
    String cep,
    BigDecimal latitude,
    BigDecimal longitude,
    Boolean padrao,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {
    public static EnderecoClienteResponse from(EnderecoCliente endereco) {
        return new EnderecoClienteResponse(
            endereco.getId(),
            endereco.getCliente().getId(),
            endereco.getRotulo(),
            endereco.getLogradouro(),
            endereco.getNumero(),
            endereco.getComplemento(),
            endereco.getBairro(),
            endereco.getCidade(),
            endereco.getEstado(),
            endereco.getCep(),
            endereco.getLatitude(),
            endereco.getLongitude(),
            endereco.getPadrao(),
            endereco.getCriadoEm(),
            endereco.getAtualizadoEm()
        );
    }
}
