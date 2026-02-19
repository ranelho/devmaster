package com.devmaster.application.api;

import com.devmaster.application.api.request.ClienteRequest;
import com.devmaster.application.api.response.ClienteResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/clientes")
public interface ClienteApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse criar(@Valid @RequestBody ClienteRequest request);
}
