package com.devmaster.application.api;


import com.devmaster.application.api.request.EnderecoClienteRequest;
import com.devmaster.application.api.response.EnderecoClienteResponse;
import com.devmaster.handler.APIException;
import com.devmaster.service.EnderecoClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EnderecoClienteRestController implements EnderecoClienteApi {

    private final EnderecoClienteService enderecoClienteService;

    @Override
    public ResponseEntity<List<EnderecoClienteResponse>> findAll() {
        final var enderecos = this.enderecoClienteService.findAll();
        return ResponseEntity.ok(EnderecoClienteResponse.convert(enderecos));
    }

    @Override
    public ResponseEntity<Page<EnderecoClienteResponse>> findAllPageable(Pageable pageable) {
        final var enderecos = this.enderecoClienteService.findAllPageable(pageable);
        return ResponseEntity.ok(EnderecoClienteResponse.convertPageable(enderecos));
    }

    @Override
    public ResponseEntity<EnderecoClienteResponse> findById(Long id) {
        final var response = new EnderecoClienteResponse(this.enderecoClienteService.findById(id));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<EnderecoClienteResponse>> findAllByClienteId(Long id) {
        throw APIException.build(HttpStatus.NOT_IMPLEMENTED, "Rota não implementada");
    }

    @Override
    public ResponseEntity<EnderecoClienteResponse> criar(EnderecoClienteRequest request) {
        final var response = new EnderecoClienteResponse(this.enderecoClienteService.criar(request));
        final var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Override
    public ResponseEntity<EnderecoClienteResponse> atualizar(Long id, EnderecoClienteRequest request) {
        final var response = new EnderecoClienteResponse(this.enderecoClienteService.atualizar(id, request));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<EnderecoClienteResponse> alterarPadrao(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        this.enderecoClienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
