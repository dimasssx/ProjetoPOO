package negocio.exceptions;

public class SessaoJaExisteException extends Exception {
    public SessaoJaExisteException(){
            super("A Sessao ja existe");
    }
}

