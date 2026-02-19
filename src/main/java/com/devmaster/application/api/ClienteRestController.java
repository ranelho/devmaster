package com.devmaster.application.api;

import com.devmaster.application.api.request.ClienteRequest;
import com.devmaster.application.api.response.ClienteResponse;
import com.devmaster.service.ClienteService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClienteRestController implements ClienteApi {

    private final ClienteService service;


    @Override
    public ClienteResponse criar(ClienteRequest request) {
        return service.criar(request);
    }
}
