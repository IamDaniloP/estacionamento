package com.danilo_pereira.estacionamento.dto;

import com.danilo_pereira.estacionamento.model.TipoVeiculo;

public record CadastrarVeiculoRequest(
        String placa,
        String modelo,
        String cor,
        TipoVeiculo tipo
) {
}
