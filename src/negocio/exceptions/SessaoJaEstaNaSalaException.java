package negocio.exceptions;

public class SessaoJaEstaNaSalaException extends Exception {
    public SessaoJaEstaNaSalaException(){
        super("Sessao ja cadastrada");
    }
}
