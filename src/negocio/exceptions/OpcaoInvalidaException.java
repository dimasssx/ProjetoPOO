package negocio.exceptions;

public class OpcaoInvalidaException extends Exception{
    public OpcaoInvalidaException() {
        super("Opcao invalida selecionada");
    }
}
