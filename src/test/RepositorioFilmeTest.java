package test;

import dados.RepositorioFilmesArquivoBinario;
import dados.RepositorioSessoesArquivoBinario;
import negocio.FilmesNegocio;
import negocio.entidades.Filme;
import negocio.entidades.Sala;
import negocio.entidades.Sala2D;
import negocio.entidades.Sessao;
import negocio.exceptions.filmes.FilmeJaEstaNoCatalogoException;
import negocio.exceptions.filmes.FilmeNaoEstaCadastradoException;
import negocio.exceptions.filmes.NenhumFilmeEncontradoException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;

class RepositorioFilmeTest {

    private FilmesNegocio filmesNegocio;
    private RepositorioFilmesArquivoBinario repFilmes;
    private RepositorioSessoesArquivoBinario repSessoes;
    private final String DIRETORIOARQUIVOFILMES = "filmesTest.bin";
    private final String DIRETORIOARQUIVOSESSOES = "sessaoTest.bin"; // tem que usar pois remover um filme do catálogo que tenha uma sessao relacionada, tambem remove essa sessao

    @BeforeEach
    void setUp() {
        repFilmes = new RepositorioFilmesArquivoBinario(DIRETORIOARQUIVOFILMES);
        repSessoes = new RepositorioSessoesArquivoBinario(DIRETORIOARQUIVOSESSOES);
        filmesNegocio = new FilmesNegocio(repFilmes, repSessoes);
    }
    @AfterEach
    void excluir() {
        File f = new File(DIRETORIOARQUIVOFILMES);
        File j = new File(DIRETORIOARQUIVOSESSOES);
        if (f.exists()) f.delete();
        if(j.exists()) j.delete();
    }

    @Test
    void testAdicionar() throws Exception{
        filmesNegocio.adicionarFilme("Batman", "Ação", "2h32", "12");
        ArrayList<Filme> catalogo = filmesNegocio.listarCatalogo();
        assertEquals(1, catalogo.size());
        assertEquals("Batman", catalogo.get(0).getTitulo());
    }

    @Test
    void testFalharAdicionar() throws Exception {
        filmesNegocio.adicionarFilme("Batman", "Ação", "2h32", "12");
        assertThrows(FilmeJaEstaNoCatalogoException.class, () -> filmesNegocio.adicionarFilme("Batman", "Ação", "2h32", "12"));
    }

    @Test
    void testRemover() throws Exception{
        filmesNegocio.adicionarFilme("Batman", "Ação", "2h32", "12");
        Filme filme = filmesNegocio.procurarFilmePorTitulo("Batman");
        filmesNegocio.removerFilme(filme.getId());
    }
    @Test
    void falhaRemover(){
        Filme filme = new Filme("Batman","Ação", "2h32", "12");
       assertThrows(FilmeNaoEstaCadastradoException.class,()->filmesNegocio.removerFilme(filme.getId())) ;
       assertThrows(FilmeNaoEstaCadastradoException.class,()->filmesNegocio.removerFilme("IDXXXXXX"));
    }
    @Test
    void removerFilmeEExcluirSessoesRelacionadas() throws Exception {
        Sala sala = new Sala2D("SALA 1",12,12);
        filmesNegocio.adicionarFilme("Interestelar", "Ficção ciencitica", "2h50", "12");
        Filme filme = filmesNegocio.procurarFilmePorTitulo("Interestelar");
        Sessao s1 = new Sessao(filme, "15:00", sala, "23-04");
        repSessoes.adicionarSessao(s1);
        filmesNegocio.removerFilme(filme.getId());

        assertTrue(repFilmes.listarFilmes().isEmpty());
        assertTrue(repSessoes.procurarSessoesPorIdDoFilme(filme.getId()).isEmpty());
        assertTrue(repSessoes.retornarTodas().isEmpty());

    }
    @Test
    void verificaSeRemoveSessaoCoreta() throws Exception{
        Sala sala = new Sala2D("SALA 1",12,12);

        filmesNegocio.adicionarFilme("Interestelar", "Ficção ciencitica", "2h50", "12");
        Filme filme = filmesNegocio.procurarFilmePorTitulo("Interestelar");
        filmesNegocio.adicionarFilme("batman","acao","2h","15");
        Filme filme2 = filmesNegocio.procurarFilmePorTitulo("batman");

        Sessao s1 = new Sessao(filme, "15:00", sala, "23-04");
        Sessao s2 = new Sessao(filme2, "15:00", sala, "23-04");

        repSessoes.adicionarSessao(s1);
        repSessoes.adicionarSessao(s2);

        filmesNegocio.removerFilme(filme.getId());

        assertEquals(1, filmesNegocio.listarCatalogo().size());
        assertTrue(repSessoes.procurarSessoesPorIdDoFilme(filme.getId()).isEmpty());
        assertFalse(repSessoes.retornarTodas().isEmpty());
        assertFalse(repSessoes.procurarSessoesPorIdDoFilme(filme2.getId()).isEmpty());
    }

    @Test
    void atualizarFilmePorID() throws Exception {
        filmesNegocio.adicionarFilme("Batman", "Ação", "2h32", "12");
        Filme f = repFilmes.listarFilmes().get(0);
        filmesNegocio.atualizarFilmePorID(f.getId(), "Batman o cavaleiro das trevas", "Ação,Suspense", "2h30", "16");
        Filme atualizado = filmesNegocio.procurarFilmePorID(f.getId());
        assertEquals("Batman o cavaleiro das trevas", atualizado.getTitulo());
        assertEquals("Ação,Suspense", atualizado.getGenero());
    }
    @Test
    void FalhaAtualizarFilmePorID() throws Exception {
        filmesNegocio.adicionarFilme("Batman", "Ação", "2h32", "12");
        assertThrows(FilmeNaoEstaCadastradoException.class,()->filmesNegocio.atualizarFilmePorID("FI0912", "Batman o cavaleiro das trevas", "Ação,Suspense", "2h30", "16"));
    }
    @Test
    void testCatalogovazio() {
        assertThrows(NenhumFilmeEncontradoException.class,()-> filmesNegocio.listarCatalogo());
    }
}
