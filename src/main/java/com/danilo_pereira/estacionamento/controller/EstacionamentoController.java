package com.danilo_pereira.estacionamento.controller;

import com.danilo_pereira.estacionamento.dto.MovimentacaoResponse;
import com.danilo_pereira.estacionamento.dto.RegistrarEntradaRequest;
import com.danilo_pereira.estacionamento.dto.RegistrarSaidaRequest;
import com.danilo_pereira.estacionamento.service.HistoricoMovimentacoesService;
import com.danilo_pereira.estacionamento.service.ListarEstacionadosService;
import com.danilo_pereira.estacionamento.service.RegistrarEntradaService;
import com.danilo_pereira.estacionamento.service.RegistrarSaidaService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estacionamento")
public class EstacionamentoController {

    private final RegistrarEntradaService registrarEntradaService;
    private final RegistrarSaidaService registrarSaidaService;
    private final ListarEstacionadosService listarEstacionadosService;
    private final HistoricoMovimentacoesService historicoMovimentacoesService;

    public EstacionamentoController(
            RegistrarEntradaService registrarEntradaService,
            RegistrarSaidaService registrarSaidaService,
            ListarEstacionadosService listarEstacionadosService,
            HistoricoMovimentacoesService historicoMovimentacoesService
    ) {
        this.registrarEntradaService = registrarEntradaService;
        this.registrarSaidaService = registrarSaidaService;
        this.listarEstacionadosService = listarEstacionadosService;
        this.historicoMovimentacoesService = historicoMovimentacoesService;
    }

    @PostMapping("/entradas")
    @ResponseStatus(HttpStatus.CREATED)
    public MovimentacaoResponse registrarEntrada(@RequestBody RegistrarEntradaRequest request) {
        return MovimentacaoResponse.from(registrarEntradaService.executar(request));
    }

    @PostMapping("/saidas")
    public MovimentacaoResponse registrarSaida(@RequestBody RegistrarSaidaRequest request) {
        return MovimentacaoResponse.from(registrarSaidaService.executar(request));
    }

    @GetMapping("/estacionados")
    public List<MovimentacaoResponse> listarEstacionados() {
        return listarEstacionadosService.executar()
                .stream()
                .map(MovimentacaoResponse::from)
                .toList();
    }

    @GetMapping("/historico")
    public List<MovimentacaoResponse> historico() {
        return historicoMovimentacoesService.executar()
                .stream()
                .map(MovimentacaoResponse::from)
                .toList();
    }
}
