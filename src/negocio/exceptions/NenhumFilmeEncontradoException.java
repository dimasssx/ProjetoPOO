package negocio.exceptions;

public class NenhumFilmeEncontradoException extends Exception {
    public NenhumFilmeEncontradoException(){
        super("Nenhum filme foi encontrado no catalógo");
    }
}
