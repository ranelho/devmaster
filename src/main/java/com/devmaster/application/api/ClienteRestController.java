package com.devmaster.application.api;

import com.devmaster.application.api.request.ClienteRequest;
import com.devmaster.application.api.response.ClienteResponse;
import com.devmaster.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClienteRestController implements ClienteApi {

    private final ClienteService clienteService;

    @Override
    public ResponseEntity<List<ClienteResponse>> findAll() {
        final var clientes = this.clienteService.findAll();
        return ResponseEntity.ok(ClienteResponse.convert(clientes));
    }

    @Override
    public ResponseEntity<ClienteResponse> findById(Long id) {
        final var response = new ClienteResponse(this.clienteService.findById(id));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ClienteResponse> criar(ClienteRequest request) {
        final var response = new ClienteResponse(this.clienteService.criar(request));
        final var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Override
    public ResponseEntity<ClienteResponse> atualizar(Long id, ClienteRequest request) {
        final var response = new ClienteResponse((this.clienteService.atualizar(id, request)));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ClienteResponse> alternarAtivo(Long id) {
        final var response = new ClienteResponse(this.clienteService.alternarAtivo(id));
        return ResponseEntity.ok(response);
    }
}
