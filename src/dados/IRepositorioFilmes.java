package dados;

//import exceptions.FilmeJaEstaNoCatalogoException;
//import exceptions.FilmeNaoEstaCadastradoException;
import java.util.ArrayList;

import negocio.entidades.Filme;

public interface IRepositorioFilmes {

    void adicionarFilme(Filme filme); 
    void removerFilme(String nome); 
    void removerFilme(Filme filme); 
    void atualizaFilme(Filme filme); 
    Filme procurarFilme(Filme filme);
    Filme procurarFilme(String nome);
    boolean existe (Filme filme);
    ArrayList listarFilmes();
}