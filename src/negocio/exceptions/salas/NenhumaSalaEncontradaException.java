package negocio.exceptions.salas;

public class NenhumaSalaEncontradaException extends Exception {
    public NenhumaSalaEncontradaException() {
        super("AVISO: Nenhuma sala esta cadastrada no sistema, nesse momento!");
    }
}
