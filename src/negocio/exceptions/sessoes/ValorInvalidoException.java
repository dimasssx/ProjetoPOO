package negocio.exceptions.sessoes;

public class ValorInvalidoException extends Exception {
    public ValorInvalidoException() {
        super("Valor de ingresso inválido. O valor deve ser maior que zero.");
    }
} 