package com.danilo_pereira.estacionamento.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Veiculo veiculo;

    @ManyToOne(optional = false)
    private Vaga vaga;

    @Column(nullable = false)
    private LocalDateTime dataEntrada;

    private LocalDateTime dataSaida;

    private BigDecimal valorPago;

    protected Movimentacao() {
    }

    public Movimentacao(Veiculo veiculo, Vaga vaga, LocalDateTime dataEntrada) {
        if (dataEntrada == null) {
            throw new IllegalArgumentException("Data de entrada e obrigatoria.");
        }
        this.veiculo = veiculo;
        this.vaga = vaga;
        this.dataEntrada = dataEntrada;
    }

    public void registrarSaida(LocalDateTime dataSaida) {
        if (this.dataEntrada == null) {
            throw new IllegalStateException("Nao e possivel registrar saida sem data de entrada.");
        }
        if (this.dataSaida != null) {
            throw new IllegalStateException("Saida ja registrada.");
        }
        this.valorPago = veiculo.calcularValor(dataEntrada, dataSaida);
        this.dataSaida = dataSaida;
        this.vaga.liberar();
    }

    public Long getId() {
        return id;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }
}
