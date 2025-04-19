package negocio.exceptions.usuario;

public class ClienteNaoEncontradoException extends Exception {
    public ClienteNaoEncontradoException() {
        super("O cliente nao foi encontrado");
    }
}
