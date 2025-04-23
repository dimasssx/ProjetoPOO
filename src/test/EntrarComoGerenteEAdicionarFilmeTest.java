//Implementado por José Portela

package test;

import fachada.FachadaGerente;
import negocio.entidades.Filme;
import negocio.exceptions.filmes.FilmeJaEstaNoCatalogoException;
import negocio.exceptions.filmes.FilmeNaoEstaCadastradoException;
import negocio.exceptions.filmes.NenhumFilmeEncontradoException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EntrarComoGerenteEAdicionarFilmeTest {

    private FachadaGerente fachada;

    //acessar a fachada de gerente que usa os metodos do fluxo das classes
    @BeforeEach
    public void setup() {
        fachada = new FachadaGerente();
    }

    @Test
    public void testAdicionarFilmeComSucesso() throws FilmeJaEstaNoCatalogoException, FilmeNaoEstaCadastradoException, NenhumFilmeEncontradoException {
        String titulo = "Interestelar";
        String genero = "Ficção Científica";
        String duracao = "2h49";
        String classificacao = "12";

        fachada.adicionarFilme(titulo, genero, duracao, classificacao);

        ArrayList<String> catalogo = fachada.imprimirCatalogo();
        assertEquals(1, catalogo.size());

        Filme filme = fachada.procurarFilmePorTitulo(titulo);
        assertNotNull(filme);
        assertEquals(titulo, filme.getTitulo());
        assertEquals(genero, filme.getGenero());
        assertEquals(duracao, filme.getDuracao());
        assertEquals(classificacao, filme.getClassificacao());

        System.out.println("Filme adicionado com sucesso e validado no catálogo!");
    }
}
