package negocio.exceptions;

public class SalaNaoEncontradaException extends Exception {
    public SalaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}