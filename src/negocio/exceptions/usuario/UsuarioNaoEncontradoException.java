package negocio.exceptions.usuario;

public class UsuarioNaoEncontradoException extends Exception{
    public UsuarioNaoEncontradoException() {
        super("Usuario nao foi encontrado");
    }
}
