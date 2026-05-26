package com.danilo_pereira.estacionamento.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.danilo_pereira.estacionamento.dto.CriarVagasRequest;
import com.danilo_pereira.estacionamento.exception.RegraNegocioException;
import com.danilo_pereira.estacionamento.model.Vaga;
import com.danilo_pereira.estacionamento.repository.VagaRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VagaServiceTest {

    @Mock
    private VagaRepository vagaRepository;

    @InjectMocks
    private VagaService service;

    @Test
    void deveCriarVagasDeUmADezQuandoNaoExistiremVagas() {
        when(vagaRepository.findAll()).thenReturn(List.of());
        when(vagaRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Vaga> vagas = service.criar(new CriarVagasRequest(10));

        assertEquals(10, vagas.size());
        assertEquals("1", vagas.get(0).getNumero());
        assertEquals("10", vagas.get(9).getNumero());
    }

    @Test
    void deveContinuarNumeracaoIncrementalAPartirDaMaiorVagaExistente() {
        when(vagaRepository.findAll()).thenReturn(List.of(new Vaga("1"), new Vaga("10")));
        when(vagaRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Vaga> vagas = service.criar(new CriarVagasRequest(3));

        assertEquals("11", vagas.get(0).getNumero());
        assertEquals("12", vagas.get(1).getNumero());
        assertEquals("13", vagas.get(2).getNumero());
    }

    @Test
    void deveSalvarTodasAsVagasGeradas() {
        when(vagaRepository.findAll()).thenReturn(List.of());
        when(vagaRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
        ArgumentCaptor<List<Vaga>> captor = ArgumentCaptor.forClass(List.class);

        service.criar(new CriarVagasRequest(2));

        verify(vagaRepository).saveAll(captor.capture());
        assertEquals("1", captor.getValue().get(0).getNumero());
        assertEquals("2", captor.getValue().get(1).getNumero());
    }

    @Test
    void deveImpedirQuantidadeNula() {
        assertThrows(RegraNegocioException.class, () -> service.criar(new CriarVagasRequest(null)));
    }

    @Test
    void deveImpedirQuantidadeZero() {
        assertThrows(RegraNegocioException.class, () -> service.criar(new CriarVagasRequest(0)));
    }

    @Test
    void deveListarVagas() {
        List<Vaga> vagas = List.of(new Vaga("1"), new Vaga("2"));
        when(vagaRepository.findAll()).thenReturn(vagas);

        assertEquals(vagas, service.listar());
    }
}
