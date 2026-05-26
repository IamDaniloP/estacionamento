package com.danilo_pereira.estacionamento.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.danilo_pereira.estacionamento.dto.RegistrarEntradaRequest;
import com.danilo_pereira.estacionamento.exception.RegraNegocioException;
import com.danilo_pereira.estacionamento.model.Carro;
import com.danilo_pereira.estacionamento.model.Movimentacao;
import com.danilo_pereira.estacionamento.model.Vaga;
import com.danilo_pereira.estacionamento.model.Veiculo;
import com.danilo_pereira.estacionamento.repository.MovimentacaoRepository;
import com.danilo_pereira.estacionamento.repository.VagaRepository;
import com.danilo_pereira.estacionamento.repository.VeiculoRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegistrarEntradaServiceTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private VagaRepository vagaRepository;

    @Mock
    private MovimentacaoRepository movimentacaoRepository;

    @InjectMocks
    private RegistrarEntradaService service;

    @Test
    void deveRegistrarEntradaComDataHoraAtualEOcuparVaga() {
        Veiculo veiculo = new Carro("ABC1234", "Onix", "Preto");
        Vaga vaga = new Vaga("1");
        when(movimentacaoRepository.existsByVeiculoPlacaAndDataSaidaIsNull("ABC1234")).thenReturn(false);
        when(veiculoRepository.findByPlaca("ABC1234")).thenReturn(Optional.of(veiculo));
        when(vagaRepository.findByNumero("1")).thenReturn(Optional.of(vaga));
        when(movimentacaoRepository.existsByVagaAndDataSaidaIsNull(vaga)).thenReturn(false);
        when(movimentacaoRepository.save(any(Movimentacao.class))).thenAnswer(invocation -> invocation.getArgument(0));
        LocalDateTime antes = LocalDateTime.now();

        Movimentacao movimentacao = service.executar(new RegistrarEntradaRequest("abc1234", "1"));

        LocalDateTime depois = LocalDateTime.now();
        assertEquals(veiculo, movimentacao.getVeiculo());
        assertEquals(vaga, movimentacao.getVaga());
        assertFalse(movimentacao.getDataEntrada().isBefore(antes));
        assertFalse(movimentacao.getDataEntrada().isAfter(depois));
        assertTrue(vaga.isOcupada());
    }

    @Test
    void deveSalvarMovimentacaoCriada() {
        Veiculo veiculo = new Carro("ABC1234", "Onix", "Preto");
        Vaga vaga = new Vaga("1");
        when(movimentacaoRepository.existsByVeiculoPlacaAndDataSaidaIsNull("ABC1234")).thenReturn(false);
        when(veiculoRepository.findByPlaca("ABC1234")).thenReturn(Optional.of(veiculo));
        when(vagaRepository.findByNumero("1")).thenReturn(Optional.of(vaga));
        when(movimentacaoRepository.existsByVagaAndDataSaidaIsNull(vaga)).thenReturn(false);
        when(movimentacaoRepository.save(any(Movimentacao.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ArgumentCaptor<Movimentacao> captor = ArgumentCaptor.forClass(Movimentacao.class);

        service.executar(new RegistrarEntradaRequest("ABC1234", "1"));

        verify(movimentacaoRepository).save(captor.capture());
        assertEquals("ABC1234", captor.getValue().getVeiculo().getPlaca());
        assertEquals("1", captor.getValue().getVaga().getNumero());
    }

    @Test
    void deveImpedirEntradaSemPlaca() {
        assertThrows(RegraNegocioException.class, () -> service.executar(new RegistrarEntradaRequest(" ", "1")));
    }

    @Test
    void deveImpedirEntradaSemVaga() {
        assertThrows(RegraNegocioException.class, () -> service.executar(new RegistrarEntradaRequest("ABC1234", " ")));
    }

    @Test
    void deveImpedirEntradaDeVeiculoJaEstacionado() {
        when(movimentacaoRepository.existsByVeiculoPlacaAndDataSaidaIsNull("ABC1234")).thenReturn(true);

        assertThrows(RegraNegocioException.class, () -> service.executar(new RegistrarEntradaRequest("ABC1234", "1")));

        verify(movimentacaoRepository, never()).save(any());
    }

    @Test
    void deveImpedirEntradaDeVeiculoNaoCadastrado() {
        when(movimentacaoRepository.existsByVeiculoPlacaAndDataSaidaIsNull("ABC1234")).thenReturn(false);
        when(veiculoRepository.findByPlaca("ABC1234")).thenReturn(Optional.empty());

        assertThrows(RegraNegocioException.class, () -> service.executar(new RegistrarEntradaRequest("ABC1234", "1")));
    }

    @Test
    void deveImpedirEntradaEmVagaNaoCadastrada() {
        Veiculo veiculo = new Carro("ABC1234", "Onix", "Preto");
        when(movimentacaoRepository.existsByVeiculoPlacaAndDataSaidaIsNull("ABC1234")).thenReturn(false);
        when(veiculoRepository.findByPlaca("ABC1234")).thenReturn(Optional.of(veiculo));
        when(vagaRepository.findByNumero("1")).thenReturn(Optional.empty());

        assertThrows(RegraNegocioException.class, () -> service.executar(new RegistrarEntradaRequest("ABC1234", "1")));
    }

    @Test
    void deveImpedirUsoDeVagaJaOcupadaPeloAtributoDaVaga() {
        Veiculo veiculo = new Carro("ABC1234", "Onix", "Preto");
        Vaga vaga = new Vaga("1");
        vaga.ocupar();
        when(movimentacaoRepository.existsByVeiculoPlacaAndDataSaidaIsNull("ABC1234")).thenReturn(false);
        when(veiculoRepository.findByPlaca("ABC1234")).thenReturn(Optional.of(veiculo));
        when(vagaRepository.findByNumero("1")).thenReturn(Optional.of(vaga));

        assertThrows(RegraNegocioException.class, () -> service.executar(new RegistrarEntradaRequest("ABC1234", "1")));
    }

    @Test
    void deveImpedirUsoDeVagaComMovimentacaoAberta() {
        Veiculo veiculo = new Carro("ABC1234", "Onix", "Preto");
        Vaga vaga = new Vaga("1");
        when(movimentacaoRepository.existsByVeiculoPlacaAndDataSaidaIsNull("ABC1234")).thenReturn(false);
        when(veiculoRepository.findByPlaca("ABC1234")).thenReturn(Optional.of(veiculo));
        when(vagaRepository.findByNumero("1")).thenReturn(Optional.of(vaga));
        when(movimentacaoRepository.existsByVagaAndDataSaidaIsNull(vaga)).thenReturn(true);

        assertThrows(RegraNegocioException.class, () -> service.executar(new RegistrarEntradaRequest("ABC1234", "1")));
    }
}
