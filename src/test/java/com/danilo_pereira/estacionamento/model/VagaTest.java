package com.danilo_pereira.estacionamento.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class VagaTest {

    @Test
    void deveOcuparELiberarVaga() {
        Vaga vaga = new Vaga("1");

        vaga.ocupar();
        assertTrue(vaga.isOcupada());

        vaga.liberar();
        assertFalse(vaga.isOcupada());
    }

    @Test
    void deveImpedirOcuparVagaJaOcupada() {
        Vaga vaga = new Vaga("1");
        vaga.ocupar();

        assertThrows(IllegalStateException.class, vaga::ocupar);
    }
}
