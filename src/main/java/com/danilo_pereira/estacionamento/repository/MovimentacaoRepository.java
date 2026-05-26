package com.danilo_pereira.estacionamento.repository;

import com.danilo_pereira.estacionamento.model.Movimentacao;
import com.danilo_pereira.estacionamento.model.Vaga;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    boolean existsByVeiculoPlacaAndDataSaidaIsNull(String placa);

    boolean existsByVagaAndDataSaidaIsNull(Vaga vaga);

    Optional<Movimentacao> findByVeiculoPlacaAndDataSaidaIsNull(String placa);

    List<Movimentacao> findByDataSaidaIsNullOrderByDataEntradaAsc();

    List<Movimentacao> findAllByOrderByDataEntradaDesc();
}
