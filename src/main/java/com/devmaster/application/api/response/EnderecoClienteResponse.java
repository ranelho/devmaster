package com.devmaster.application.api.response;

import com.devmaster.domain.EnderecoCliente;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public record EnderecoClienteResponse(
        Long id,
        String bairro,
        String cep,
        String cidade,
        String estado,
        String logradouro,
        String numero,
        String complemento,
        String rotulo,
        Boolean padrao,
        Long clienteId,
        BigDecimal latitude,
        BigDecimal longitude
) {
    public EnderecoClienteResponse(EnderecoCliente enderecoCliente) {
        this(
                enderecoCliente.getId(),
                enderecoCliente.getBairro(),
                enderecoCliente.getCep(),
                enderecoCliente.getCidade(),
                enderecoCliente.getEstado(),
                enderecoCliente.getLogradouro(),
                enderecoCliente.getNumero(),
                enderecoCliente.getComplemento(),
                enderecoCliente.getRotulo(),
                enderecoCliente.getPadrao(),
                enderecoCliente.getCliente().getId(),
                enderecoCliente.getLatitude(),
                enderecoCliente.getLongitude()
        );
    }

    public static List<EnderecoClienteResponse> convert(List<EnderecoCliente> list) {
        return list.stream().map(EnderecoClienteResponse::new).toList();
    }

    public static Page<EnderecoClienteResponse> convertPageable(Page<EnderecoCliente> list) {
        return list.map(EnderecoClienteResponse::new);
    }
}
