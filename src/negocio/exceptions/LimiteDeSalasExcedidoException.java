package negocio.exceptions;

public class LimiteDeSalasExcedidoException extends Exception {
    public LimiteDeSalasExcedidoException(String mensagem) {
        super(mensagem);
    }
}