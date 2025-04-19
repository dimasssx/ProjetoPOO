package negocio.exceptions.usuario;

public class SenhaInvalidaException extends Exception {
    public SenhaInvalidaException() {
        super("A senha deve possuir 8 caracteres");
    }
}
