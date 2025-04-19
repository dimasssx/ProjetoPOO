package negocio.exceptions.sessoes;

public class SessaoJaEstaNaSalaException extends Exception {
    public SessaoJaEstaNaSalaException(){
        super("Sessao ja cadastrada");
    }
}
