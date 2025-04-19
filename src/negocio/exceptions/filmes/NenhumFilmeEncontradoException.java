package negocio.exceptions.filmes;

public class NenhumFilmeEncontradoException extends Exception {
    public NenhumFilmeEncontradoException(){
        super("Nenhum filme foi encontrado no catalógo");
    }
}
