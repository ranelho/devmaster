package com.devmaster.application.service.impl;

import com.devmaster.application.api.request.EnderecoClienteRequest;
import com.devmaster.application.api.response.EnderecoClienteResponse;
import com.devmaster.application.service.EnderecoClienteService;
import com.devmaster.domain.Cliente;
import com.devmaster.domain.EnderecoCliente;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.ClienteRepository;
import com.devmaster.infrastructure.repository.EnderecoClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de EnderecoCliente.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class EnderecoClienteApplicationService implements EnderecoClienteService {
    
    private final EnderecoClienteRepository enderecoRepository;
    private final ClienteRepository clienteRepository;
    
    @Override
    @Transactional
    public EnderecoClienteResponse adicionarEndereco(Long clienteId, EnderecoClienteRequest request) {
        Cliente cliente = buscarClienteOuFalhar(clienteId);
        
        // Se for marcado como padrão, remover padrão dos outros
        if (Boolean.TRUE.equals(request.padrao())) {
            enderecoRepository.removerPadraoTodosEnderecos(clienteId);
        }
        
        // Se for o primeiro endereço, marcar como padrão automaticamente
        boolean isPrimeiroEndereco = enderecoRepository.countByClienteId(clienteId) == 0;
        
        EnderecoCliente endereco = EnderecoCliente.builder()
            .cliente(cliente)
            .rotulo(request.rotulo())
            .logradouro(request.logradouro())
            .numero(request.numero())
            .complemento(request.complemento())
            .bairro(request.bairro())
            .cidade(request.cidade())
            .estado(request.estado())
            .cep(request.cep())
            .latitude(request.latitude())
            .longitude(request.longitude())
            .padrao(isPrimeiroEndereco || Boolean.TRUE.equals(request.padrao()))
            .build();
        
        endereco = enderecoRepository.save(endereco);
        
        return EnderecoClienteResponse.from(endereco);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EnderecoClienteResponse> listarEnderecos(Long clienteId) {
        buscarClienteOuFalhar(clienteId);
        
        return enderecoRepository.findByClienteId(clienteId)
            .stream()
            .map(EnderecoClienteResponse::from)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public EnderecoClienteResponse buscarEndereco(Long clienteId, Long enderecoId) {
        EnderecoCliente endereco = enderecoRepository.findByIdAndClienteId(enderecoId, clienteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Endereço não encontrado"));
        
        return EnderecoClienteResponse.from(endereco);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EnderecoClienteResponse buscarEnderecoPadrao(Long clienteId) {
        buscarClienteOuFalhar(clienteId);
        
        EnderecoCliente endereco = enderecoRepository.findByClienteIdAndPadrao(clienteId, true)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Cliente não possui endereço padrão"));
        
        return EnderecoClienteResponse.from(endereco);
    }
    
    @Override
    @Transactional
    public EnderecoClienteResponse atualizarEndereco(
        Long clienteId,
        Long enderecoId,
        EnderecoClienteRequest request
    ) {
        EnderecoCliente endereco = enderecoRepository.findByIdAndClienteId(enderecoId, clienteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Endereço não encontrado"));
        
        // Atualizar campos
        if (request.rotulo() != null) endereco.setRotulo(request.rotulo());
        if (request.logradouro() != null) endereco.setLogradouro(request.logradouro());
        if (request.numero() != null) endereco.setNumero(request.numero());
        if (request.complemento() != null) endereco.setComplemento(request.complemento());
        if (request.bairro() != null) endereco.setBairro(request.bairro());
        if (request.cidade() != null) endereco.setCidade(request.cidade());
        if (request.estado() != null) endereco.setEstado(request.estado());
        if (request.cep() != null) endereco.setCep(request.cep());
        if (request.latitude() != null) endereco.setLatitude(request.latitude());
        if (request.longitude() != null) endereco.setLongitude(request.longitude());
        
        // Se for marcado como padrão, remover padrão dos outros
        if (Boolean.TRUE.equals(request.padrao()) && !endereco.getPadrao()) {
            enderecoRepository.removerPadraoTodosEnderecos(clienteId);
            endereco.definirComoPadrao();
        }
        
        endereco = enderecoRepository.save(endereco);
        
        return EnderecoClienteResponse.from(endereco);
    }
    
    @Override
    @Transactional
    public EnderecoClienteResponse definirEnderecoPadrao(Long clienteId, Long enderecoId) {
        EnderecoCliente endereco = enderecoRepository.findByIdAndClienteId(enderecoId, clienteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Endereço não encontrado"));
        
        // Remover padrão dos outros endereços
        enderecoRepository.removerPadraoTodosEnderecos(clienteId);
        
        // Definir como padrão
        endereco.definirComoPadrao();
        endereco = enderecoRepository.save(endereco);
        
        return EnderecoClienteResponse.from(endereco);
    }
    
    @Override
    @Transactional
    public void removerEndereco(Long clienteId, Long enderecoId) {
        EnderecoCliente endereco = enderecoRepository.findByIdAndClienteId(enderecoId, clienteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Endereço não encontrado"));
        
        // Se for o endereço padrão e houver outros, definir outro como padrão
        if (endereco.getPadrao()) {
            List<EnderecoCliente> outrosEnderecos = enderecoRepository.findByClienteId(clienteId)
                .stream()
                .filter(e -> !e.getId().equals(enderecoId))
                .toList();
            
            if (!outrosEnderecos.isEmpty()) {
                EnderecoCliente novoPadrao = outrosEnderecos.get(0);
                novoPadrao.definirComoPadrao();
                enderecoRepository.save(novoPadrao);
            }
        }
        
        enderecoRepository.delete(endereco);
    }
    
    private Cliente buscarClienteOuFalhar(Long clienteId) {
        return clienteRepository.findById(clienteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }
}
