package com.danilo_pereira.estacionamento.service;

import com.danilo_pereira.estacionamento.dto.RegistrarEntradaRequest;
import com.danilo_pereira.estacionamento.exception.RegraNegocioException;
import com.danilo_pereira.estacionamento.model.Movimentacao;
import com.danilo_pereira.estacionamento.model.Vaga;
import com.danilo_pereira.estacionamento.model.Veiculo;
import com.danilo_pereira.estacionamento.repository.MovimentacaoRepository;
import com.danilo_pereira.estacionamento.repository.VagaRepository;
import com.danilo_pereira.estacionamento.repository.VeiculoRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrarEntradaService {

    private final VeiculoRepository veiculoRepository;
    private final VagaRepository vagaRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    public RegistrarEntradaService(
            VeiculoRepository veiculoRepository,
            VagaRepository vagaRepository,
            MovimentacaoRepository movimentacaoRepository
    ) {
        this.veiculoRepository = veiculoRepository;
        this.vagaRepository = vagaRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @Transactional
    public Movimentacao executar(RegistrarEntradaRequest request) {
        String placa = normalizarPlaca(request.placa());
        String numeroVaga = normalizarTexto(request.numeroVaga());
        LocalDateTime dataEntrada = LocalDateTime.now();

        if (placa == null || placa.isBlank()) {
            throw new RegraNegocioException("Placa e obrigatoria.");
        }
        if (numeroVaga == null || numeroVaga.isBlank()) {
            throw new RegraNegocioException("Vaga ocupada e obrigatoria.");
        }
        if (movimentacaoRepository.existsByVeiculoPlacaAndDataSaidaIsNull(placa)) {
            throw new RegraNegocioException("Veiculo com esta placa ja esta estacionado.");
        }

        Veiculo veiculo = veiculoRepository.findByPlaca(placa)
                .orElseThrow(() -> new RegraNegocioException("Veiculo nao cadastrado."));
        Vaga vaga = vagaRepository.findByNumero(numeroVaga)
                .orElseThrow(() -> new RegraNegocioException("Vaga nao cadastrada."));

        if (vaga.isOcupada() || movimentacaoRepository.existsByVagaAndDataSaidaIsNull(vaga)) {
            throw new RegraNegocioException("Vaga ja esta ocupada.");
        }

        vaga.ocupar();
        Movimentacao movimentacao = new Movimentacao(veiculo, vaga, dataEntrada);
        return movimentacaoRepository.save(movimentacao);
    }

    private String normalizarPlaca(String placa) {
        return placa == null ? null : placa.trim().toUpperCase();
    }

    private String normalizarTexto(String texto) {
        return texto == null ? null : texto.trim().toUpperCase();
    }
}
