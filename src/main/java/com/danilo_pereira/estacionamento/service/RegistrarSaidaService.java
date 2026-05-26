package com.danilo_pereira.estacionamento.service;

import com.danilo_pereira.estacionamento.dto.RegistrarSaidaRequest;
import com.danilo_pereira.estacionamento.exception.RegraNegocioException;
import com.danilo_pereira.estacionamento.model.Movimentacao;
import com.danilo_pereira.estacionamento.repository.MovimentacaoRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrarSaidaService {

    private final MovimentacaoRepository movimentacaoRepository;

    public RegistrarSaidaService(MovimentacaoRepository movimentacaoRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @Transactional
    public Movimentacao executar(RegistrarSaidaRequest request) {
        String placa = request.placa() == null ? null : request.placa().trim().toUpperCase();
        LocalDateTime dataSaida = LocalDateTime.now();

        if (placa == null || placa.isBlank()) {
            throw new RegraNegocioException("Placa e obrigatoria.");
        }

        Movimentacao movimentacao = movimentacaoRepository.findByVeiculoPlacaAndDataSaidaIsNull(placa)
                .orElseThrow(() -> new RegraNegocioException("Veiculo nao esta estacionado."));

        movimentacao.registrarSaida(dataSaida);
        return movimentacaoRepository.save(movimentacao);
    }
}
