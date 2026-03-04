package com.devmaster.application.api.response;

import com.devmaster.domain.EnderecoCliente;
import com.devmaster.domain.EnderecoPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public static EnderecoClienteResponse from(EnderecoPedido endereco) {
        if (endereco == null) return null;
        return new EnderecoClienteResponse(
            null, // ID não existe em EnderecoPedido (embeddable)
            null, // ClienteID não existe diretamente em EnderecoPedido
            null, // Rótulo não existe em EnderecoPedido
            endereco.getLogradouro(),
            endereco.getNumero(),
            endereco.getComplemento(),
            endereco.getBairro(),
            endereco.getCidade(),
            endereco.getEstado(),
            endereco.getCep(),
            endereco.getLatitude(),
            endereco.getLongitude(),
            false, // Padrao não aplicável
            null, // CriadoEm não aplicável
            null  // AtualizadoEm não aplicável
        );
    }
}
