package negocio;

import dados.IRepositorioFilmes;
import negocio.entidades.Filme;
import negocio.exceptions.filmes.FilmeJaEstaNoCatalogoException;
import negocio.exceptions.filmes.FilmeNaoEstaCadastradoException;
import negocio.exceptions.filmes.NenhumFilmeEncontradoException;

import java.util.ArrayList;

public class FilmesNegocio {
    IRepositorioFilmes repositorioFilmes;

    public FilmesNegocio(IRepositorioFilmes repositorioFilmes){
        this.repositorioFilmes = repositorioFilmes;
    }

    public void adicionarFilme(String nome,String genero,String duracao,String classificacao) throws FilmeJaEstaNoCatalogoException {
       Filme filme = new Filme(nome,genero,duracao,classificacao);
        if (repositorioFilmes.existe(filme)){
            throw new FilmeJaEstaNoCatalogoException();
        }else{
            repositorioFilmes.adicionarFilme(filme);
        }
    }
    public void removerFilme(String filme) throws FilmeNaoEstaCadastradoException {
        Filme filmeprocurado = repositorioFilmes.procurarFilme(filme);
        if (filmeprocurado != null) repositorioFilmes.removerFilme(filmeprocurado);
        else throw new FilmeNaoEstaCadastradoException();
    }
    public void atualizarFilme(String nome,String genero,String duracao,String classificacao) throws FilmeNaoEstaCadastradoException{
        Filme filme = new Filme(nome,genero,duracao,classificacao);
        if (repositorioFilmes.existe(filme)) repositorioFilmes.atualizaFilme(filme);
        else throw new FilmeNaoEstaCadastradoException();
    }
    public Filme procurarFilme(String filme) throws FilmeNaoEstaCadastradoException {
        Filme filmeprocurado = repositorioFilmes.procurarFilme(filme);
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
