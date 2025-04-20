package dados;

import java.util.ArrayList;

import negocio.entidades.Filme;

public interface IRepositorioFilmes {
    void adicionarFilme(Filme filme);
    void removerFilme(Filme filme);
    void atualizaFilme(Filme filme);
    Filme procurarFilmePorID(String ID);
    Filme procurarFilmePorTitulo(String nome);
    boolean existe (Filme filme);
    ArrayList<Filme> listarFilmes();
}