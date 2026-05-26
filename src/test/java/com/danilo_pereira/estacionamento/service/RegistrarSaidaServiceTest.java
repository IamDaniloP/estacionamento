package com.danilo_pereira.estacionamento.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.danilo_pereira.estacionamento.dto.RegistrarSaidaRequest;
import com.danilo_pereira.estacionamento.exception.RegraNegocioException;
import com.danilo_pereira.estacionamento.model.Carro;
import com.danilo_pereira.estacionamento.model.Movimentacao;
import com.danilo_pereira.estacionamento.model.Vaga;
import com.danilo_pereira.estacionamento.repository.MovimentacaoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegistrarSaidaServiceTest {

    @Mock
    private MovimentacaoRepository movimentacaoRepository;

    @InjectMocks
    private RegistrarSaidaService service;

    @Test
    void deveRegistrarSaidaComDataHoraAtualCalcularValorELiberarVaga() {
        Vaga vaga = new Vaga("1");
        vaga.ocupar();
        Movimentacao movimentacao = new Movimentacao(
                new Carro("ABC1234", "Onix", "Preto"),
                vaga,
                LocalDateTime.now().minusMinutes(30)
        );
        when(movimentacaoRepository.findByVeiculoPlacaAndDataSaidaIsNull("ABC1234"))
                .thenReturn(Optional.of(movimentacao));
        when(movimentacaoRepository.save(any(Movimentacao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Movimentacao resultado = service.executar(new RegistrarSaidaRequest("abc1234"));

        assertNotNull(resultado.getDataSaida());
        assertEquals(new BigDecimal("5.00"), resultado.getValorPago());
        assertFalse(vaga.isOcupada());
        verify(movimentacaoRepository).save(movimentacao);
    }

    @Test
    void deveImpedirSaidaSemPlaca() {
        assertThrows(RegraNegocioException.class, () -> service.executar(new RegistrarSaidaRequest(" ")));
    }

    @Test
    void deveImpedirSaidaDeVeiculoNaoEstacionado() {
        when(movimentacaoRepository.findByVeiculoPlacaAndDataSaidaIsNull("ABC1234")).thenReturn(Optional.empty());

        assertThrows(RegraNegocioException.class, () -> service.executar(new RegistrarSaidaRequest("ABC1234")));

        verify(movimentacaoRepository, never()).save(any());
    }
}
