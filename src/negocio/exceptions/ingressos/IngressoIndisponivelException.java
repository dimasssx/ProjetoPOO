package negocio.exceptions.ingressos;

public class IngressoIndisponivelException extends Exception {
    public IngressoIndisponivelException() {
        super("Ingresso indisponível");
    }
}
