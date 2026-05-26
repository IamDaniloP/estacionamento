package com.danilo_pereira.estacionamento.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.danilo_pereira.estacionamento.dto.CadastrarVeiculoRequest;
import com.danilo_pereira.estacionamento.exception.RegraNegocioException;
import com.danilo_pereira.estacionamento.model.Caminhonete;
import com.danilo_pereira.estacionamento.model.Carro;
import com.danilo_pereira.estacionamento.model.Moto;
import com.danilo_pereira.estacionamento.model.TipoVeiculo;
import com.danilo_pereira.estacionamento.model.Veiculo;
import com.danilo_pereira.estacionamento.repository.VeiculoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CadastrarVeiculoServiceTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    @InjectMocks
    private CadastrarVeiculoService service;

    @Test
    void deveCadastrarCarro() {
        when(veiculoRepository.existsByPlaca("ABC1234")).thenReturn(false);
        when(veiculoRepository.save(any(Veiculo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Veiculo veiculo = service.executar(new CadastrarVeiculoRequest("abc1234", "Onix", "Preto", TipoVeiculo.CARRO));

        assertInstanceOf(Carro.class, veiculo);
        assertEquals("ABC1234", veiculo.getPlaca());
        assertEquals(TipoVeiculo.CARRO, veiculo.getTipo());
    }

    @Test
    void deveCadastrarMoto() {
        when(veiculoRepository.existsByPlaca("ABC1234")).thenReturn(false);
        when(veiculoRepository.save(any(Veiculo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Veiculo veiculo = service.executar(new CadastrarVeiculoRequest("ABC1234", "Biz", "Vermelha", TipoVeiculo.MOTO));

        assertInstanceOf(Moto.class, veiculo);
        assertEquals(TipoVeiculo.MOTO, veiculo.getTipo());
    }

    @Test
    void deveCadastrarCaminhonete() {
        when(veiculoRepository.existsByPlaca("ABC1234")).thenReturn(false);
        when(veiculoRepository.save(any(Veiculo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Veiculo veiculo = service.executar(new CadastrarVeiculoRequest("ABC1234", "S10", "Branca", TipoVeiculo.CAMINHONETE));

        assertInstanceOf(Caminhonete.class, veiculo);
        assertEquals(TipoVeiculo.CAMINHONETE, veiculo.getTipo());
    }

    @Test
    void deveImpedirCadastroDePlacaDuplicada() {
        when(veiculoRepository.existsByPlaca("ABC1234")).thenReturn(true);

        assertThrows(RegraNegocioException.class, () -> service.executar(
                new CadastrarVeiculoRequest("ABC1234", "Onix", "Preto", TipoVeiculo.CARRO)
        ));

        verify(veiculoRepository, never()).save(any());
    }

    @Test
    void deveImpedirCadastroSemPlaca() {
        assertThrows(RegraNegocioException.class, () -> service.executar(
                new CadastrarVeiculoRequest(" ", "Onix", "Preto", TipoVeiculo.CARRO)
        ));
    }

    @Test
    void deveImpedirCadastroSemModelo() {
        assertThrows(RegraNegocioException.class, () -> service.executar(
                new CadastrarVeiculoRequest("ABC1234", " ", "Preto", TipoVeiculo.CARRO)
        ));
    }

    @Test
    void deveImpedirCadastroSemCor() {
        assertThrows(RegraNegocioException.class, () -> service.executar(
                new CadastrarVeiculoRequest("ABC1234", "Onix", " ", TipoVeiculo.CARRO)
        ));
    }

    @Test
    void deveImpedirCadastroSemTipo() {
        assertThrows(RegraNegocioException.class, () -> service.executar(
                new CadastrarVeiculoRequest("ABC1234", "Onix", "Preto", null)
        ));
    }
}
