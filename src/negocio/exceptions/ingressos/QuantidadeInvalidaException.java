package negocio.exceptions.ingressos;

public class QuantidadeInvalidaException extends Exception {
    public QuantidadeInvalidaException() {
        super("Quantidade de ingressos inválida. Deve ser um número positivo.");
    }
    
    public QuantidadeInvalidaException(String mensagem) {
        super(mensagem);
    }
} 