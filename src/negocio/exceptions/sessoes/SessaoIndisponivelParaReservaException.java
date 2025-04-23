package negocio.exceptions.sessoes;

public class SessaoIndisponivelParaReservaException extends Exception{
    public SessaoIndisponivelParaReservaException(){super("A sessão não está viável para reserva, existem já asentos reservados, ou não possue espaço físico para essa quantidade de pessoas!");}
}
