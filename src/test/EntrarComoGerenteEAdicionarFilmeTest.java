//Implementado por José Portela

package test;

import dados.RepositorioClientesArquivoBinario;
import dados.RepositorioFilmesArquivoBinario;
import dados.RepositorioSalasArquivoBinario;
import dados.RepositorioSessoesArquivoBinario;
import fachada.FachadaGerente;
import negocio.entidades.Filme;
import negocio.exceptions.filmes.FilmeJaEstaNoCatalogoException;
import negocio.exceptions.filmes.FilmeNaoEstaCadastradoException;
import negocio.exceptions.filmes.NenhumFilmeEncontradoException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EntrarComoGerenteEAdicionarFilmeTest {

    private FachadaGerente fachada;
    private RepositorioFilmesArquivoBinario repositorioFilmes;
    private RepositorioSessoesArquivoBinario repositorioSessoes;
    private RepositorioSalasArquivoBinario repositorioSalas;
    private File fileFilmes;
    private File fileSessoes;
    private File fileSalas;

    //acessar a fachada de gerente que usa os metodos do fluxo das classes
    @BeforeEach
    public void setup() throws IOException {
        fileFilmes = new File("filmetest.bin");
        fileSessoes = new File("sessoestest.bin");
        fileSalas = new File("salastest.bin");

        fileFilmes.createNewFile();
        fileSessoes.createNewFile();
        fileSalas.createNewFile();

        repositorioFilmes = new RepositorioFilmesArquivoBinario(fileFilmes.getPath());
        repositorioSessoes = new RepositorioSessoesArquivoBinario(fileSessoes.getPath());
        repositorioSalas = new RepositorioSalasArquivoBinario(fileSalas.getPath());

        fachada = new FachadaGerente(repositorioFilmes, repositorioSalas,repositorioSessoes);

    }
    @AfterEach
    public void delete(){
        if (fileFilmes.exists()) fileFilmes.delete();
        if (fileSessoes.exists()) fileSessoes.delete();
        if (fileSalas.exists()) fileSalas.delete();
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
