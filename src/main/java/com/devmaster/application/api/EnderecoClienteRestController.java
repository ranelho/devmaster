package com.devmaster.application.api;

import com.devmaster.application.api.request.EnderecoClienteRequest;
import com.devmaster.application.api.response.EnderecoClienteResponse;
import com.devmaster.application.api.response.MessageResponse;
import com.devmaster.application.service.EnderecoClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST para gestão de endereços de clientes.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class EnderecoClienteRestController implements EnderecoClienteAPI {
    
    private final EnderecoClienteService enderecoService;
    
    @Override
    public EnderecoClienteResponse adicionarEndereco(Long clienteId, EnderecoClienteRequest request) {
        return enderecoService.adicionarEndereco(clienteId, request);
    }
    
    @Override
    public List<EnderecoClienteResponse> listarEnderecos(Long clienteId) {
        return enderecoService.listarEnderecos(clienteId);
    }
    
    @Override
    public EnderecoClienteResponse buscarEndereco(Long clienteId, Long enderecoId) {
        return enderecoService.buscarEndereco(clienteId, enderecoId);
    }
    
    @Override
    public EnderecoClienteResponse buscarEnderecoPadrao(Long clienteId) {
        return enderecoService.buscarEnderecoPadrao(clienteId);
    }
    
    @Override
    public EnderecoClienteResponse atualizarEndereco(
        Long clienteId,
        Long enderecoId,
        EnderecoClienteRequest request
    ) {
        return enderecoService.atualizarEndereco(clienteId, enderecoId, request);
    }
    
    @Override
    public EnderecoClienteResponse definirEnderecoPadrao(Long clienteId, Long enderecoId) {
        return enderecoService.definirEnderecoPadrao(clienteId, enderecoId);
    }
    
    @Override
    public MessageResponse removerEndereco(Long clienteId, Long enderecoId) {
        enderecoService.removerEndereco(clienteId, enderecoId);
        return new MessageResponse("Endereço removido com sucesso");
    }
}
