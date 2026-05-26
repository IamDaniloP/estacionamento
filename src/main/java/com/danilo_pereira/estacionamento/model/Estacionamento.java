package com.danilo_pereira.estacionamento.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Estacionamento {

    private final String nome;

    public Estacionamento(String nome) {
        this.nome = nome;
    }

    public BigDecimal calcularValor(Movimentacao movimentacao, LocalDateTime dataSaida) {
        return movimentacao.getVeiculo().calcularValor(movimentacao.getDataEntrada(), dataSaida);
    }

    public String getNome() {
        return nome;
    }
}
