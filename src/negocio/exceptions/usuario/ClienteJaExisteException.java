package negocio.exceptions.usuario;

public class ClienteJaExisteException extends Exception {
    public ClienteJaExisteException() {
        super("O cliente ja existe");
    }
}