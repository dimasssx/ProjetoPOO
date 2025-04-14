package exceptions;

public class CodigoSalaJaExisteException extends Exception {
    public CodigoSalaJaExisteException() {
        super("Já existe uma sala com esse código");
    }
}
