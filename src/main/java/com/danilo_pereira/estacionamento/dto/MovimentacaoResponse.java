package com.danilo_pereira.estacionamento.dto;

import com.danilo_pereira.estacionamento.model.Movimentacao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimentacaoResponse(
        Long id,
        VeiculoResponse veiculo,
        VagaResponse vaga,
        LocalDateTime dataEntrada,
        LocalDateTime dataSaida,
        BigDecimal valorPago
) {
    public static MovimentacaoResponse from(Movimentacao movimentacao) {
        return new MovimentacaoResponse(
                movimentacao.getId(),
                VeiculoResponse.from(movimentacao.getVeiculo()),
                VagaResponse.from(movimentacao.getVaga()),
                movimentacao.getDataEntrada(),
                movimentacao.getDataSaida(),
                movimentacao.getValorPago()
        );
    }
}
