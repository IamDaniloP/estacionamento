package com.danilo_pereira.estacionamento.controller;

import com.danilo_pereira.estacionamento.dto.CriarVagasRequest;
import com.danilo_pereira.estacionamento.dto.VagaResponse;
import com.danilo_pereira.estacionamento.service.VagaService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vagas")
public class VagaController {

    private final VagaService vagaService;

    public VagaController(VagaService vagaService) {
        this.vagaService = vagaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<VagaResponse> criar(@RequestBody CriarVagasRequest request) {
        return vagaService.criar(request)
                .stream()
                .map(VagaResponse::from)
                .toList();
    }

    @GetMapping
    public List<VagaResponse> listar() {
        return vagaService.listar()
                .stream()
                .map(VagaResponse::from)
                .toList();
    }
}
