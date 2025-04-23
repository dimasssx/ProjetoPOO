package negocio.exceptions.usuario;

public class UsuarioJaExisteException extends Exception {
    public UsuarioJaExisteException() {
        super("Esse usuário já está em utilização");
    }
}