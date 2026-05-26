package com.danilo_pereira.estacionamento.service;

import com.danilo_pereira.estacionamento.model.Movimentacao;
import com.danilo_pereira.estacionamento.repository.MovimentacaoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class HistoricoMovimentacoesService {

    private final MovimentacaoRepository movimentacaoRepository;

    public HistoricoMovimentacoesService(MovimentacaoRepository movimentacaoRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
    }

    public List<Movimentacao> executar() {
        return movimentacaoRepository.findAllByOrderByDataEntradaDesc();
    }
}
