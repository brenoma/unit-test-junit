package br.ce.wcaquino.servicos;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.builders.FilmeBuilder.*;
import static br.ce.wcaquino.builders.LocacaoBuilder.*;
import static br.ce.wcaquino.builders.UsuarioBuilder.*;
import static br.ce.wcaquino.matchers.MatchersProprios.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LocacaoServiceTest extends LocacaoService {

    @InjectMocks
    private LocacaoService service = new LocacaoService();

    @Mock
    private SPCService spc;

    @Mock
    private LocacaoDAO dao;

    @Mock
    private EmailService email;

    private static int testCounter;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        service = new LocacaoService();
//        dao = mock(LocacaoDAO.class);
//        service.setLocacaoDAO(dao);
//        spc = mock(SPCService.class);
//        service.setSPCService(spc);
//        email = mock(EmailService.class);
//        service.setEmailService(email);
    }

    @After
    public void tearDown() {
        testCounter++;
    }

    @BeforeClass
    public static void setupClass() {
        System.out.println("Before Class");
        testCounter = 0;
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("After Class");
        System.out.println(testCounter);
    }

    @Test
    public void deveAlugarFilme() throws Exception {
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        // cen??rio
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

        System.out.println("Teste!");

        // a????o
        Locacao locacao = service.alugarFilme(usuario, filmes);

        // verifica????o
//        Assertions.assertEquals(5.00, locacao.getValor(), 0.01);
//        Assertions.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
//        Assertions.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));

        error.checkThat(locacao.getValor(), is(5.0));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
    }

    // Forma elegante se se criar um teste
    @Test(expected = FilmeSemEstoqueException.class)
    public void alugarFilme_withNoInventory_shouldThrowException() throws Exception {

        // cen??rio
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());

        // a????o
        Locacao locacao = service.alugarFilme(usuario, filmes);
    }

    // Forma robusta de se criar um teste
    @Test
    public void naoDeveAlugarFilmeSemEstoque2() {

        // cen??rio
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());

        // a????o
        try {
            service.alugarFilme(usuario, filmes);
            fail("Deveria ter lan??ado uma exce????o!");
        } catch (Exception e) {
            assertThat(e.getMessage(), is("Filme sem estoque"));
        }
    }

    // Forma nova de se criar um teste
    @Test
    public void naoDeveAlugarFilmeSemEstoque3() throws Exception {

        // cen??rio
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());

        exception.expect(Exception.class);
        exception.expectMessage("Filme sem estoque");

        // a????o
        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
        //cen??rio
        List<Filme> filmes = Arrays.asList(umFilme().agora());
        Usuario usuario = umUsuario().agora();

        //a????o
        try {
            service.alugarFilme(null, filmes);
            fail("Usu??rio n??o est?? vazio");
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usu??rio vazio"));
        }

        System.out.println("Forma Robusta");
    }

    @Test
    public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
        //cen??rio
        Usuario usuario = umUsuario().agora();

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        //a????o
        service.alugarFilme(usuario, null);

        System.out.println("Forma Nova");
    }

    @Test
    public void naoDeveDevolverFilmeNoDomingo() throws FilmeSemEstoqueException, LocadoraException {
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cen??rio
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        //a????o
        Locacao retorno = service.alugarFilme(usuario, filmes);

        //verifica????o
//        boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
//        assertTrue(ehSegunda);
//        assertThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY));
        assertThat(retorno.getDataRetorno(), caiNumaSegunda());
    }

    @Test
    public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
        //cen??rio
        Usuario usuario = umUsuario().agora();
        Usuario usuario2 = umUsuario().comNome("Usu??rio 2").agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        when(spc.possuiNegativacao(usuario)).thenReturn(true);

//        exception.expect(LocadoraException.class);
//        exception.expectMessage("Usu??rio negativado");

        //a????o
        try {
            service.alugarFilme(usuario, filmes);
            fail();
        } catch (LocadoraException | FilmeSemEstoqueException e) {
            assertThat(e.getMessage(), is("Usu??rio negativado"));
        }

        //verifica????o
        verify(spc).possuiNegativacao(usuario);
    }

    @Test
    public void deveEnviarEmailParaLocacoesAtrasadas() {
        //cen??rio
        Usuario usuario = umUsuario().agora();
        Usuario usuario2 = umUsuario().comNome("Usu??rio 2").agora();
        List<Locacao> locacoes = Arrays.asList(
                umLocacao()
                        .comUsuario(usuario)
                        .comDataRetorno(DataUtils.obterDataComDiferencaDias(-2))
                        .agora());
        when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

        //a????o
        service.notificarAtrasos();

        //vericifa????o
        verify(email).notificarAtraso(usuario);
    }

    @Test
    public void deveTratarErroNoSPC() throws Exception {
        //cen??rio
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catastr??fica"));

        //verifica????o
        exception.expect(LocadoraException.class);
        exception.expectMessage("SPC fora do ar, tente novamente");

        //a????o
        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void deveProrrogarUmaLocacao() {
        //cen??rio
        Locacao locacao = umLocacao().agora();

        //a????o
        service.prorrogarLocacao(locacao, 3);

        //verifica????o
        ArgumentCaptor<Locacao> argumentCaptor = ArgumentCaptor.forClass(Locacao.class);
        verify(dao).salvar(argumentCaptor.capture());
        Locacao locacaoRetornada = argumentCaptor.getValue();

        error.checkThat(locacaoRetornada.getValor(), is(12.0));
    }
}
