package com.danilo_pereira.estacionamento.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("CAMINHONETE")
public class Caminhonete extends Veiculo {

    protected Caminhonete() {
    }

    public Caminhonete(String placa, String modelo, String cor) {
        super(placa, modelo, cor, TipoVeiculo.CAMINHONETE);
    }

    @Override
    protected BigDecimal getMultiplicador() {
        return BigDecimal.valueOf(1.5);
    }
}
