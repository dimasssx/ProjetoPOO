package negocio.exceptions.filmes;

public class FilmeJaEstaNoCatalogoException extends Exception {
    public FilmeJaEstaNoCatalogoException() {
        super("O filme ja esta no catalogo");
    }
}
