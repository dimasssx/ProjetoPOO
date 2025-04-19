package negocio.exceptions.salas;

public class CodigoSalaJaExisteException extends Exception {
    public CodigoSalaJaExisteException(String s) {
        super("Já existe uma sala com esse código");
    }
}
