package negocio.exceptions.usuario;

public class ClienteNaoEncontradoException extends Exception {
    public ClienteNaoEncontradoException() {
        super("O usuário nao foi encontrado, credenciais digitadas incorretas!");
    }
}
