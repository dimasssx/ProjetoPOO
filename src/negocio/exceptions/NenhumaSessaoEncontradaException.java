package negocio.exceptions;

public class NenhumaSessaoEncontradaException extends Exception {
    public NenhumaSessaoEncontradaException(){
        super("Nao foi encontrada nenhuma sessao");
    }
}
