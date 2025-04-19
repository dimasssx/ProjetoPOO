package negocio.entidades;

import java.io.Serial;
import java.io.Serializable;
import java.security.SecureRandom;

public class GeradorID implements Serializable {
    @Serial
    private static final long serialVersionUID = -4009776605163947718L;
    private static GeradorID instancia;
    private final SecureRandom random;
    
    private GeradorID() {
        this.random = new SecureRandom();
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
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    
    // prefixos de IDs dos objetos p diferenciar melhor eles
    public static final String PREFIXO_CLIENTE = "CL";
    public static final String PREFIXO_SALA = "SA";
    public static final String PREFIXO_SESSAO = "SE";
    public static final String PREFIXO_FILME = "FI";
    public static final String PREFIXO_INGRESSO = "IN";
} 