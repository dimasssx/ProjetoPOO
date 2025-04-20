package negocio.exceptions.pagamento;

public class PagamentoInvalidoException extends Exception {
    public PagamentoInvalidoException() {
        super("Método de pagamento inválido. Escolha uma opção válida.");
    }
} 