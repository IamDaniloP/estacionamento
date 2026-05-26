package com.danilo_pereira.estacionamento.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("CARRO")
public class Carro extends Veiculo {

    protected Carro() {
    }

    public Carro(String placa, String modelo, String cor) {
        super(placa, modelo, cor, TipoVeiculo.CARRO);
    }

    @Override
    protected BigDecimal getMultiplicador() {
        return BigDecimal.ONE;
    }
}
