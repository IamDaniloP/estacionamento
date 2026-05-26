package com.danilo_pereira.estacionamento.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.danilo_pereira.estacionamento.model.Carro;
import com.danilo_pereira.estacionamento.model.Movimentacao;
import com.danilo_pereira.estacionamento.model.Vaga;
import com.danilo_pereira.estacionamento.repository.MovimentacaoRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListarEstacionadosServiceTest {

    @Mock
    private MovimentacaoRepository movimentacaoRepository;

    @InjectMocks
    private ListarEstacionadosService service;

    @Test
    void deveListarSomenteMovimentacoesEmAbertoRetornadasPeloRepositorio() {
        List<Movimentacao> movimentacoes = List.of(new Movimentacao(
                new Carro("ABC1234", "Onix", "Preto"),
                new Vaga("1"),
                LocalDateTime.now()
        ));
        when(movimentacaoRepository.findByDataSaidaIsNullOrderByDataEntradaAsc()).thenReturn(movimentacoes);

        assertEquals(movimentacoes, service.executar());
    }
}
