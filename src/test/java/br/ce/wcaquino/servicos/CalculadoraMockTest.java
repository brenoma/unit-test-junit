package br.ce.wcaquino.servicos;

import br.ce.wcaquino.servicos.Calculadora;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class CalculadoraMockTest {

    @Mock
    private Calculadora calcMock;

    @Spy
    private Calculadora calcSpy;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void devoMostrarDiferencaEntreMockSpy() {
//        Mockito.when(calcMock.somar(1, 2)).thenReturn(8);
        Mockito.when(calcMock.somar(1, 2)).thenCallRealMethod();
        Mockito.when(calcSpy.somar(1, 2)).thenReturn(8);

        // MOCK: Quando não sabe o que fazer retorna o valor default do Mock que é 0 no caso.
        System.out.println("Mock: " + calcMock.somar(1, 2));

        /*  SPY: Quando não sabe o que fazer, ele retorna a execução método real da classe. Por conta disso
        *   o spy não funciona com interfaces, apenas com classes concretas.
        */
        System.out.println("Spy: " + calcSpy.somar(1, 2));
    }

    @Test
    public void teste() {
        Calculadora calc = Mockito.mock(Calculadora.class);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.when(calc.somar(argumentCaptor.capture(), argumentCaptor.capture())).thenReturn(5);

        Assert.assertEquals(5, calc.somar(134345, -234));
        System.out.println(argumentCaptor.getAllValues());
    }
}
