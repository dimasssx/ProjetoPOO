package negocio.exceptions.sessoes;

public class NenhumaSessaoEncontradaException extends Exception {
    public NenhumaSessaoEncontradaException(){
        super("AVISO: Nenhuma sessao está cadastrada no sistema, nesse momento!");
    }
}
