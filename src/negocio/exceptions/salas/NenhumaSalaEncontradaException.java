package negocio.exceptions.salas;

public class NenhumaSalaEncontradaException extends Exception {
    public NenhumaSalaEncontradaException() {
        super("Nenhuma sala foi encontrada");
    }
}
