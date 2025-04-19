package negocio.exceptions.geral;

public class OpcaoInvalidaException extends Exception{
    public OpcaoInvalidaException() {
        super("Opcao invalida selecionada");
    }
}
