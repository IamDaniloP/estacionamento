package com.danilo_pereira.estacionamento.service;

import com.danilo_pereira.estacionamento.dto.CadastrarVeiculoRequest;
import com.danilo_pereira.estacionamento.exception.RegraNegocioException;
import com.danilo_pereira.estacionamento.model.Caminhonete;
import com.danilo_pereira.estacionamento.model.Carro;
import com.danilo_pereira.estacionamento.model.Moto;
import com.danilo_pereira.estacionamento.model.TipoVeiculo;
import com.danilo_pereira.estacionamento.model.Veiculo;
import com.danilo_pereira.estacionamento.repository.VeiculoRepository;
import org.springframework.stereotype.Service;

@Service
public class CadastrarVeiculoService {

    private final VeiculoRepository veiculoRepository;

    public CadastrarVeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    public Veiculo executar(CadastrarVeiculoRequest request) {
        String placa = normalizarPlaca(request.placa());
        validarTexto(placa, "Placa e obrigatoria.");
        validarTexto(request.modelo(), "Modelo e obrigatorio.");
        validarTexto(request.cor(), "Cor e obrigatoria.");

        if (request.tipo() == null) {
            throw new RegraNegocioException("Tipo do veiculo e obrigatorio.");
        }
        if (veiculoRepository.existsByPlaca(placa)) {
            throw new RegraNegocioException("Ja existe veiculo cadastrado com esta placa.");
        }

        Veiculo veiculo = criarVeiculo(placa, request.modelo(), request.cor(), request.tipo());
        return veiculoRepository.save(veiculo);
    }

    private Veiculo criarVeiculo(String placa, String modelo, String cor, TipoVeiculo tipo) {
        return switch (tipo) {
            case CARRO -> new Carro(placa, modelo, cor);
            case MOTO -> new Moto(placa, modelo, cor);
            case CAMINHONETE -> new Caminhonete(placa, modelo, cor);
        };
    }

    private String normalizarPlaca(String placa) {
        return placa == null ? null : placa.trim().toUpperCase();
    }

    private void validarTexto(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new RegraNegocioException(mensagem);
        }
    }
}
