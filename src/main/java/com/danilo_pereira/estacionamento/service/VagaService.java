package com.danilo_pereira.estacionamento.service;

import com.danilo_pereira.estacionamento.dto.CriarVagasRequest;
import com.danilo_pereira.estacionamento.exception.RegraNegocioException;
import com.danilo_pereira.estacionamento.model.Vaga;
import com.danilo_pereira.estacionamento.repository.VagaRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VagaService {

    private final VagaRepository vagaRepository;

    public VagaService(VagaRepository vagaRepository) {
        this.vagaRepository = vagaRepository;
    }

    @Transactional
    public List<Vaga> criar(CriarVagasRequest request) {
        if (request.quantidade() == null || request.quantidade() <= 0) {
            throw new RegraNegocioException("Quantidade de vagas deve ser maior que zero.");
        }

        int proximoNumero = vagaRepository.findAll()
                .stream()
                .map(Vaga::getNumero)
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0) + 1;

        List<Vaga> vagas = new ArrayList<>();
        for (int i = 0; i < request.quantidade(); i++) {
            vagas.add(new Vaga(String.valueOf(proximoNumero + i)));
        }

        return vagaRepository.saveAll(vagas);
    }

    public List<Vaga> listar() {
        return vagaRepository.findAll();
    }
}
