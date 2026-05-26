package com.danilo_pereira.estacionamento;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class EstacionamentoApplicationTests {

	@Test
	void deveExecutarMetodoMainSemInstanciarContextoNosTestesUnitarios() {
		assertDoesNotThrow(() -> EstacionamentoApplication.class.getDeclaredConstructor().newInstance());
	}

}
