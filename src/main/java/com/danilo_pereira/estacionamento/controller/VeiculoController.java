package com.danilo_pereira.estacionamento.controller;

import com.danilo_pereira.estacionamento.dto.CadastrarVeiculoRequest;
import com.danilo_pereira.estacionamento.dto.VeiculoResponse;
import com.danilo_pereira.estacionamento.repository.VeiculoRepository;
import com.danilo_pereira.estacionamento.service.CadastrarVeiculoService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    private final CadastrarVeiculoService cadastrarVeiculoService;
    private final VeiculoRepository veiculoRepository;

    public VeiculoController(CadastrarVeiculoService cadastrarVeiculoService, VeiculoRepository veiculoRepository) {
        this.cadastrarVeiculoService = cadastrarVeiculoService;
        this.veiculoRepository = veiculoRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VeiculoResponse cadastrar(@RequestBody CadastrarVeiculoRequest request) {
        return VeiculoResponse.from(cadastrarVeiculoService.executar(request));
    }

    @GetMapping
    public List<VeiculoResponse> listar() {
        return veiculoRepository.findAll()
                .stream()
                .map(VeiculoResponse::from)
                .toList();
    }
}
