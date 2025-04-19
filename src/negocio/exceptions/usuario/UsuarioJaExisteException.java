package negocio.exceptions.usuario;

public class UsuarioJaExisteException extends Exception {
    public UsuarioJaExisteException() {
        super("O cliente ja existe");
    }
}