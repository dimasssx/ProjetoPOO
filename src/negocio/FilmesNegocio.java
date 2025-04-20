package negocio;

import dados.IRepositorioFilmes;
import dados.IRepositorioSessoes;
import java.util.ArrayList;
import negocio.entidades.Filme;
import negocio.entidades.Sessao;
import negocio.exceptions.filmes.FilmeJaEstaNoCatalogoException;
import negocio.exceptions.filmes.FilmeNaoEstaCadastradoException;
import negocio.exceptions.filmes.NenhumFilmeEncontradoException;

public class FilmesNegocio {
    IRepositorioFilmes repositorioFilmes;
    IRepositorioSessoes repositorioSessoes;

    public FilmesNegocio(IRepositorioFilmes repositorioFilmes, IRepositorioSessoes repositorioSessoes){
        this.repositorioFilmes = repositorioFilmes;
        this.repositorioSessoes = repositorioSessoes;
    }

    public void adicionarFilme(String nome,String genero,String duracao,String classificacao) throws FilmeJaEstaNoCatalogoException {
       Filme filme = new Filme(nome,genero,duracao,classificacao);
        if (repositorioFilmes.existe(filme)){
            throw new FilmeJaEstaNoCatalogoException();
        }else{
            repositorioFilmes.adicionarFilme(filme);
        }
    }

    public void removerFilme(String ID) throws FilmeNaoEstaCadastradoException {
        Filme filmeprocurado = repositorioFilmes.procurarFilmePorID(ID);
        ArrayList<Sessao> sessoesRemovidas;
        if (filmeprocurado != null) {
            sessoesRemovidas = repositorioSessoes.procurarSessoesPorIdDoFilme(ID);
            if (sessoesRemovidas != null) {
                for (Sessao sessao : sessoesRemovidas) {
                    repositorioSessoes.removerSessao(sessao);
                }
            }
            repositorioFilmes.removerFilme(filmeprocurado);
        }
        else throw new FilmeNaoEstaCadastradoException();
    }

    public void atualizarFilmePorID(String id, String nome, String genero, String duracao, String classificacao) throws FilmeNaoEstaCadastradoException {
        Filme filmeExistente = repositorioFilmes.procurarFilmePorID(id);
        
        if (filmeExistente == null) {
            throw new FilmeNaoEstaCadastradoException();
        }

        filmeExistente.setTitulo(nome);
        filmeExistente.setGenero(genero);
        filmeExistente.setDuracao(duracao);
        filmeExistente.setClassificacao(classificacao);

        repositorioFilmes.atualizaFilme(filmeExistente);
    }

    public Filme procurarFilmePorID(String ID) throws FilmeNaoEstaCadastradoException {
        Filme filmeprocurado = repositorioFilmes.procurarFilmePorID(ID);
        if (filmeprocurado == null){
            throw new FilmeNaoEstaCadastradoException();
        } else return filmeprocurado;
    }

    public Filme procurarFilmePorTitulo(String titulo) throws FilmeNaoEstaCadastradoException {
        Filme filmeprocurado = repositorioFilmes.procurarFilmePorTitulo(titulo);
        if (filmeprocurado == null){
            throw new FilmeNaoEstaCadastradoException();
        } else return filmeprocurado;
    }

    public ArrayList<Filme> listarCatalogo() throws NenhumFilmeEncontradoException {
        if(repositorioFilmes.listarFilmes().isEmpty()){
            throw new NenhumFilmeEncontradoException();
        }
        return repositorioFilmes.listarFilmes();
    }
}
