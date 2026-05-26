package com.danilo_pereira.estacionamento.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("MOTO")
public class Moto extends Veiculo {

    protected Moto() {
    }

    public Moto(String placa, String modelo, String cor) {
        super(placa, modelo, cor, TipoVeiculo.MOTO);
    }

    @Override
    protected BigDecimal getMultiplicador() {
        return BigDecimal.valueOf(0.5);
    }
}
