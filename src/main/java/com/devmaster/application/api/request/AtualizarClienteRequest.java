package com.devmaster.application.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AtualizarClienteRequest(
    
    @Pattern(
        regexp = "^\\(?\\d{2}\\)?\\s?9?\\d{4}-?\\d{4}$",
        message = "Telefone deve estar no formato (XX) 9XXXX-XXXX ou (XX) XXXX-XXXX"
    )
    String telefone,
    
    @Email(message = "Email deve ser válido")
    @Size(max = 255, message = "Email não pode exceder 255 caracteres")
    String email,
    
    @Size(max = 255, message = "Nome completo não pode exceder 255 caracteres")
    String nomeCompleto,
    
    @Pattern(
        regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$",
        message = "CPF deve estar no formato XXX.XXX.XXX-XX"
    )
    String cpf,
    
    @Past(message = "Data de nascimento deve ser no passado")
    LocalDate dataNascimento
) {}
