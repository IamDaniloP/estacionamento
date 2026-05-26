package com.danilo_pereira.estacionamento.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Vaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private boolean ocupada;

    protected Vaga() {
    }

    public Vaga(String numero) {
        this.numero = numero;
        this.ocupada = false;
    }

    public void ocupar() {
        if (ocupada) {
            throw new IllegalStateException("Vaga ja esta ocupada.");
        }
        this.ocupada = true;
    }

    public void liberar() {
        this.ocupada = false;
    }

    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public boolean isOcupada() {
        return ocupada;
    }
}
