package negocio.entidades;

import java.io.Serializable;
import java.security.SecureRandom;
import java.io.Serial;

public class GeradorID implements Serializable {

    @Serial
    private static final long serialVersionUID = -4009776605163947718L;
    private static GeradorID instancia;
    private final SecureRandom random;
    
    private static final String PREFIXO_CLIENTE = "CL";
    private static final String PREFIXO_SALA = "SA";
    private static final String PREFIXO_SESSAO = "SE";
    private static final String PREFIXO_FILME = "FI";
    private static final String PREFIXO_INGRESSO = "IN";
    
    private GeradorID() {
        this.random = new SecureRandom();
    }

    public String getPrefixoCliente(){
        return PREFIXO_CLIENTE;
    }

    public String getPrefixoSala(){
        return PREFIXO_SALA;
    }

    public String getPrefixoSessao(){
        return PREFIXO_SESSAO;
    }

    public String getPrefixoFilme(){
        return PREFIXO_FILME;
    }

    public String getPrefixoIngresso(){
        return PREFIXO_INGRESSO;
    }
    
    public static synchronized GeradorID getInstancia() {
        if (instancia == null) {
            instancia = new GeradorID();
        }
        return instancia;
    }
    
    public String gerarId(String prefixo) {
        StringBuilder sb = new StringBuilder(prefixo);
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10)); // Números de 0 a 9
        }
        return sb.toString();
    }
} 