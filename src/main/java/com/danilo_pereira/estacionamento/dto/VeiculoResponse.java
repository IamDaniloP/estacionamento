package com.danilo_pereira.estacionamento.dto;

import com.danilo_pereira.estacionamento.model.TipoVeiculo;
import com.danilo_pereira.estacionamento.model.Veiculo;

public record VeiculoResponse(
        Long id,
        String placa,
        String modelo,
        String cor,
        TipoVeiculo tipo
) {
    public static VeiculoResponse from(Veiculo veiculo) {
        return new VeiculoResponse(
                veiculo.getId(),
                veiculo.getPlaca(),
                veiculo.getModelo(),
                veiculo.getCor(),
                veiculo.getTipo()
        );
    }
}
