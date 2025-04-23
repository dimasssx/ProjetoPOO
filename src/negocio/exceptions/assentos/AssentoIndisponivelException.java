package negocio.exceptions.assentos;

public class AssentoIndisponivelException extends Exception {
    public AssentoIndisponivelException() {
        super("Assento indisponível na sessão");
    }
}