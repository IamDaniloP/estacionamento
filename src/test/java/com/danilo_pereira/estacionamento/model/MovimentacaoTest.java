package com.danilo_pereira.estacionamento.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class MovimentacaoTest {

    @Test
    void deveCriarMovimentacaoComDataDeEntrada() {
        LocalDateTime entrada = LocalDateTime.of(2026, 5, 16, 10, 0);

        Movimentacao movimentacao = new Movimentacao(
                new Carro("ABC1234", "Onix", "Preto"),
                new Vaga("1"),
                entrada
        );

        assertEquals(entrada, movimentacao.getDataEntrada());
    }

    @Test
    void deveImpedirMovimentacaoSemDataDeEntrada() {
        assertThrows(IllegalArgumentException.class, () -> new Movimentacao(
                new Carro("ABC1234", "Onix", "Preto"),
                new Vaga("1"),
                null
        ));
    }

    @Test
    void deveRegistrarSaidaCalcularValorELiberarVaga() {
        LocalDateTime entrada = LocalDateTime.of(2026, 5, 16, 10, 0);
        Vaga vaga = new Vaga("1");
        vaga.ocupar();
        Movimentacao movimentacao = new Movimentacao(
                new Carro("ABC1234", "Onix", "Preto"),
                vaga,
                entrada
        );

        movimentacao.registrarSaida(entrada.plusMinutes(61));

        assertEquals(entrada.plusMinutes(61), movimentacao.getDataSaida());
        assertEquals(new BigDecimal("8.00"), movimentacao.getValorPago());
        assertFalse(vaga.isOcupada());
    }

    @Test
    void deveImpedirSaidaDuplicada() {
        LocalDateTime entrada = LocalDateTime.of(2026, 5, 16, 10, 0);
        Movimentacao movimentacao = new Movimentacao(
                new Carro("ABC1234", "Onix", "Preto"),
                new Vaga("1"),
                entrada
        );
        movimentacao.registrarSaida(entrada.plusMinutes(30));

        assertThrows(IllegalStateException.class, () -> movimentacao.registrarSaida(entrada.plusMinutes(40)));
    }

    @Test
    void deveImpedirRegistroDeSaidaSemDataDeEntrada() {
        Movimentacao movimentacao = new Movimentacao();

        assertThrows(IllegalStateException.class, () -> movimentacao.registrarSaida(LocalDateTime.now()));
    }
}
