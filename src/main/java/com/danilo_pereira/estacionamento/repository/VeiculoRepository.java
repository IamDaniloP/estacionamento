package com.danilo_pereira.estacionamento.repository;

import com.danilo_pereira.estacionamento.model.Veiculo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    boolean existsByPlaca(String placa);

    Optional<Veiculo> findByPlaca(String placa);
}
