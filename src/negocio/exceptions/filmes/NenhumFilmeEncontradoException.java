package negocio.exceptions.filmes;

public class NenhumFilmeEncontradoException extends Exception {
    public NenhumFilmeEncontradoException(){
        super("AVISO: Nenhum filme está cadastrado no catalógo, nesse momento!");
    }
}
