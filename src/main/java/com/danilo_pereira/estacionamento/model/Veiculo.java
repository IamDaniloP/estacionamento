package com.danilo_pereira.estacionamento.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_discriminador")
public abstract class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String placa;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private String cor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVeiculo tipo;

    protected Veiculo() {
    }

    protected Veiculo(String placa, String modelo, String cor, TipoVeiculo tipo) {
        this.placa = normalizarPlaca(placa);
        this.modelo = modelo;
        this.cor = cor;
        this.tipo = tipo;
    }

    public BigDecimal calcularValor(LocalDateTime entrada, LocalDateTime saida) {
        if (entrada == null) {
            throw new IllegalArgumentException("Data de entrada e obrigatoria.");
        }
        if (saida == null || saida.isBefore(entrada)) {
            throw new IllegalArgumentException("Data de saida deve ser posterior a data de entrada.");
        }

        long minutos = Math.max(1, Duration.between(entrada, saida).toMinutes());
        BigDecimal valorBase = BigDecimal.valueOf(5);

        if (minutos > 60) {
            long minutosAdicionais = minutos - 60;
            long horasAdicionais = (long) Math.ceil(minutosAdicionais / 60.0);
            valorBase = valorBase.add(BigDecimal.valueOf(horasAdicionais).multiply(BigDecimal.valueOf(3)));
        }

        return valorBase.multiply(getMultiplicador()).setScale(2, RoundingMode.HALF_UP);
    }

    protected abstract BigDecimal getMultiplicador();

    protected static String normalizarPlaca(String placa) {
        if (placa == null) {
            return null;
        }
        return placa.trim().toUpperCase();
    }

    public Long getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getModelo() {
        return modelo;
    }

    public String getCor() {
        return cor;
    }

    public TipoVeiculo getTipo() {
        return tipo;
    }
}
