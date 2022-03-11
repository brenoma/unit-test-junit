package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import br.ce.wcaquino.servicos.Calculadora;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalculadoraTest {
    private int a, b;
    private Calculadora calc;

    @Before
    public void setup() {
        a = 4;
        b = 2;
        calc = new Calculadora();
    }

    @Test
    public void deveSomarDoisValores() {
        //ação
        int resultado = calc.somar(a,b);

        //verificação
        assertEquals((a+b), resultado);
    }

    @Test
    public void deveSubtrairDoisValores() {
        //ação
        int resultado = calc.subtrair(a, b);

        //verificação
        assertEquals((a-b), resultado);
    }

    @Test
    public void deveMultiplicarDoisValores() {
        //ação
        int resultado = calc.multiplicar(a, b);

        //verificação
        assertEquals((a*b), resultado);
    }

    @Test
    public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
        //ação
        int resultado = calc.dividir(a, b);

        //verificação
        assertEquals((a/b), resultado);
    }

    @Test(expected = NaoPodeDividirPorZeroException.class)
    public void deveLancarExecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
        calc.dividir(a, 0);
    }
}
