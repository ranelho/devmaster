package com.devmaster.service;


import com.devmaster.application.api.request.ClienteRequest;
import com.devmaster.application.api.response.ClienteResponse;

public interface ClienteService {
    ClienteResponse criar(ClienteRequest request);
}
