import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrdemTest {

    // Classe para mostrar a importância da independência dos testes.

    public static int contador = 0;

    @Test
    public void inicia() {
        contador = 1;
    }

    @Test
    public void verifica() {
        assertEquals(1, contador);
    }
}
