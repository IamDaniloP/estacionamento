package com.danilo_pereira.estacionamento.dto;

public record RegistrarEntradaRequest(
        String placa,
        String numeroVaga
) {
}
