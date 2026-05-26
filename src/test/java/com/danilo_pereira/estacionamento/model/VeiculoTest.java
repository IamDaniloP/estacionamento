package com.danilo_pereira.estacionamento.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class VeiculoTest {

    private final LocalDateTime entrada = LocalDateTime.of(2026, 5, 16, 10, 0);

    @Test
    void deveCobrarCincoReaisParaCarroAteUmaHora() {
        Carro carro = new Carro("abc1234", "Onix", "Preto");

        BigDecimal valor = carro.calcularValor(entrada, entrada.plusMinutes(60));

        assertEquals(new BigDecimal("5.00"), valor);
    }

    @Test
    void deveCobrarHoraAdicionalArredondadaParaCima() {
        Carro carro = new Carro("abc1234", "Onix", "Preto");

        BigDecimal valor = carro.calcularValor(entrada, entrada.plusMinutes(61));

        assertEquals(new BigDecimal("8.00"), valor);
    }

    @Test
    void deveCobrarDuasHorasAdicionaisQuandoPassarDeDuasHoras() {
        Carro carro = new Carro("abc1234", "Onix", "Preto");

        BigDecimal valor = carro.calcularValor(entrada, entrada.plusMinutes(121));

        assertEquals(new BigDecimal("11.00"), valor);
    }

    @Test
    void motoDevePagarMetadeDoValor() {
        Moto moto = new Moto("abc1234", "Biz", "Vermelha");

        BigDecimal valor = moto.calcularValor(entrada, entrada.plusMinutes(121));

        assertEquals(new BigDecimal("5.50"), valor);
    }

    @Test
    void caminhoneteDevePagarCinquentaPorCentoAMais() {
        Caminhonete caminhonete = new Caminhonete("abc1234", "S10", "Branca");

        BigDecimal valor = caminhonete.calcularValor(entrada, entrada.plusMinutes(121));

        assertEquals(new BigDecimal("16.50"), valor);
    }

    @Test
    void deveNormalizarPlacaNoCadastroDoVeiculo() {
        Carro carro = new Carro(" abc1234 ", "Onix", "Preto");

        assertEquals("ABC1234", carro.getPlaca());
    }

    @Test
    void deveImpedirCalculoSemDataDeEntrada() {
        Carro carro = new Carro("abc1234", "Onix", "Preto");

        assertThrows(IllegalArgumentException.class, () -> carro.calcularValor(null, entrada));
    }

    @Test
    void deveImpedirCalculoSemDataDeSaida() {
        Carro carro = new Carro("abc1234", "Onix", "Preto");

        assertThrows(IllegalArgumentException.class, () -> carro.calcularValor(entrada, null));
    }

    @Test
    void deveImpedirCalculoComSaidaAntesDaEntrada() {
        Carro carro = new Carro("abc1234", "Onix", "Preto");

        assertThrows(IllegalArgumentException.class, () -> carro.calcularValor(entrada, entrada.minusMinutes(1)));
    }
}
