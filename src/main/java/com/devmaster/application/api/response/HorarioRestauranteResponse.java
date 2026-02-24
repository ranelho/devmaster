package com.devmaster.application.api.response;

import com.devmaster.domain.HorarioRestaurante;

import java.time.LocalTime;

public record HorarioRestauranteResponse(
    Long id,
    Long restauranteId,
    Integer diaSemana,
    String diaSemanaDescricao,
    LocalTime horarioAbertura,
    LocalTime horarioFechamento,
    Boolean fechado
) {
    public static HorarioRestauranteResponse from(HorarioRestaurante horario) {
        return new HorarioRestauranteResponse(
            horario.getId(),
            horario.getRestaurante().getId(),
            horario.getDiaSemana(),
            getDiaSemanaDescricao(horario.getDiaSemana()),
            horario.getHorarioAbertura(),
            horario.getHorarioFechamento(),
            horario.getFechado()
        );
    }
    
    private static String getDiaSemanaDescricao(Integer diaSemana) {
        return switch (diaSemana) {
            case 0 -> "Domingo";
            case 1 -> "Segunda-feira";
            case 2 -> "Terça-feira";
            case 3 -> "Quarta-feira";
            case 4 -> "Quinta-feira";
            case 5 -> "Sexta-feira";
            case 6 -> "Sábado";
            default -> "Desconhecido";
        };
    }
}
