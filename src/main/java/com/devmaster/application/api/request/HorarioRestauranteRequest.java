package com.devmaster.application.api.request;

import jakarta.validation.constraints.*;

import java.time.LocalTime;

public record HorarioRestauranteRequest(
    @NotNull(message = "Dia da semana é obrigatório")
    @Min(value = 0, message = "Dia da semana deve ser entre 0 (Domingo) e 6 (Sábado)")
    @Max(value = 6, message = "Dia da semana deve ser entre 0 (Domingo) e 6 (Sábado)")
    Integer diaSemana,
    
    @NotNull(message = "Horário de abertura é obrigatório")
    LocalTime horarioAbertura,
    
    @NotNull(message = "Horário de fechamento é obrigatório")
    LocalTime horarioFechamento,
    
    Boolean fechado
) {}
