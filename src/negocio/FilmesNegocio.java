package negocio;

import dados.IRepositorioFilmes;
import negocio.entidades.Filme;
import negocio.exceptions.FilmeJaEstaNoCatalogoException;
import negocio.exceptions.FilmeNaoEstaCadastradoException;
import negocio.exceptions.NenhumFilmeEncontradoException;

import java.util.ArrayList;

public class FilmesNegocio {
    IRepositorioFilmes catalogo;

    public FilmesNegocio(IRepositorioFilmes catalogo){
        this.catalogo = catalogo;
    }

    public void adicionarFilme(Filme filme) throws FilmeJaEstaNoCatalogoException {
        if (catalogo.existe(filme)){
            throw new FilmeJaEstaNoCatalogoException();
        }else{
            catalogo.adicionarFilme(filme);
        }
    }
    public void removerFilme(String filme) throws FilmeNaoEstaCadastradoException {
        Filme filmeprocurado = catalogo.procurarFilme(filme);
        if (filmeprocurado != null) catalogo.removerFilme(filmeprocurado);
        else throw new FilmeNaoEstaCadastradoException();
    }
    public void atualizarFilme(Filme filme) throws FilmeNaoEstaCadastradoException{
        if (catalogo.existe(filme)) catalogo.atualizaFilme(filme);
        else throw new FilmeNaoEstaCadastradoException();
    }
    public Filme procurarFilme(String filme) throws FilmeNaoEstaCadastradoException {
        Filme filmeprocurado = catalogo.procurarFilme(filme);
        if (filmeprocurado == null){
            throw new FilmeNaoEstaCadastradoException();
        }else return filmeprocurado;
    }
    public ArrayList<Filme> listarCatalogo() throws NenhumFilmeEncontradoException {
        if(catalogo.listarFilmes().isEmpty()){
            throw new NenhumFilmeEncontradoException();
        }
        return catalogo.listarFilmes();
    }
}
