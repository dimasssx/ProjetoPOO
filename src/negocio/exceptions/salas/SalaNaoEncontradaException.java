package negocio.exceptions.salas;

public class SalaNaoEncontradaException extends Exception {
    public SalaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}