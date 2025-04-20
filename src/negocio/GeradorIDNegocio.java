package negocio;

import dados.IRepositorioIDs;
import dados.RepositorioIDsArquivoBinario;
import java.security.SecureRandom;
import negocio.entidades.GeradorID;

public class GeradorIDNegocio {
    private static GeradorIDNegocio instancia;
    private final IRepositorioIDs repositorio;
    private final SecureRandom random;
    
    private GeradorIDNegocio() {
        this.repositorio = new RepositorioIDsArquivoBinario();
        this.random = new SecureRandom();
    }
    
    public static synchronized GeradorIDNegocio getInstancia() {
        if (instancia == null) {
            instancia = new GeradorIDNegocio();
        }
        return instancia;
    }
    
    public String gerarId(String prefixo) {
        String id;
        do {
            id = gerarNovoId(prefixo);
        } while (repositorio.verificarIdExistente(prefixo, id));
        
        repositorio.registrarId(prefixo, id);
        return id;
    }
    
    private String gerarNovoId(String prefixo) {
        StringBuilder sb = new StringBuilder(prefixo);
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10)); // Números de 0 a 9
        }
        return sb.toString();
    }
    
    // Métodos de constantes para expor os prefixos da classe GeradorID
    public String getPrefixoCliente() {
        return GeradorID.getInstancia().getPrefixoCliente();
    }
    
    public String getPrefixoSala() { return GeradorID.getInstancia().getPrefixoSala(); }
    
    public String getPrefixoSessao() {
        return GeradorID.getInstancia().getPrefixoSessao();
    }
    
    public String getPrefixoFilme() {
        return GeradorID.getInstancia().getPrefixoFilme();
    }

} 