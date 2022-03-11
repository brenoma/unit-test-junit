import br.ce.wcaquino.entidades.Usuario;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class AssertTest {

    @Test
    public void test() {
        Assert.assertTrue(true);
        Assert.assertFalse(false);
        Assert.assertEquals(1.51, 1.55, 0.05);
        Assert.assertEquals("Erro de comparação", 1, 2);

        Assert.assertEquals(Math.PI, 3.14, 0.01);
        Assert.assertNotEquals("bola", "casa");
        Assert.assertTrue("bola".equalsIgnoreCase("Bola"));

        Usuario u1 = new Usuario("Usuário 1");
        Usuario u2 = new Usuario("Usuário 1");
        Usuario u3 = u2;

        Assert.assertEquals(u1, u2);

        // Compara as instâncias dos objetos.
        Assert.assertSame(u3, u2);

        Assert.assertNotNull(u3);
    }
}
