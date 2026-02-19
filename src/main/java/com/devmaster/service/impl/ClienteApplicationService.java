package com.devmaster.service.impl;

import com.devmaster.application.api.request.ClienteRequest;
import com.devmaster.application.api.response.ClienteResponse;
import com.devmaster.domain.Cliente;
import com.devmaster.infra.ClienteRepository;
import com.devmaster.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteApplicationService implements ClienteService {

    private final ClienteRepository repository;

    @Override
    public ClienteResponse criar(ClienteRequest request) {
        //todo: Validar cpf e telefone
        //todo: se cliente existir retorna ele
        Cliente clienteSalvo = repository.save(new Cliente(request));

        return new ClienteResponse(clienteSalvo);
    }
}
