package negocio.exceptions.filmes;

public class FilmeNaoEstaCadastradoException extends Exception {
    public FilmeNaoEstaCadastradoException() {
        super("O filme nao esta no nosso catalogo");
    }
}
