package com.danilo_pereira.estacionamento.dto;

import com.danilo_pereira.estacionamento.model.Vaga;

public record VagaResponse(
        Long id,
        String numero,
        boolean ocupada
) {
    public static VagaResponse from(Vaga vaga) {
        return new VagaResponse(vaga.getId(), vaga.getNumero(), vaga.isOcupada());
    }
}
