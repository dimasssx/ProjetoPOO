package negocio.exceptions.salas;

public class LimiteDeSalasExcedidoException extends Exception {
    public LimiteDeSalasExcedidoException(String mensagem) {
        super(mensagem);
    }
}