package com.danilo_pereira.estacionamento.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class EstacionamentoTest {

    @Test
    void deveCalcularValorDeUmaMovimentacao() {
        LocalDateTime entrada = LocalDateTime.of(2026, 5, 16, 10, 0);
        Movimentacao movimentacao = new Movimentacao(
                new Carro("ABC1234", "Onix", "Preto"),
                new Vaga("1"),
                entrada
        );
        Estacionamento estacionamento = new Estacionamento("Estacionamento Central");

        BigDecimal valor = estacionamento.calcularValor(movimentacao, entrada.plusMinutes(61));

        assertEquals(new BigDecimal("8.00"), valor);
        assertEquals("Estacionamento Central", estacionamento.getNome());
    }
}
