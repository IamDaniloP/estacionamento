package com.danilo_pereira.estacionamento.repository;

import com.danilo_pereira.estacionamento.model.Vaga;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VagaRepository extends JpaRepository<Vaga, Long> {

    Optional<Vaga> findByNumero(String numero);

    boolean existsByNumero(String numero);
}
