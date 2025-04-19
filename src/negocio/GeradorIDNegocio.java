package negocio;

import dados.IRepositorioIDs;
import dados.RepositorioIDsArquivoBinario;
import negocio.entidades.GeradorID;

public class GeradorIDNegocio {
    private static GeradorIDNegocio instancia;
    private final IRepositorioIDs repositorio;
    
    private GeradorIDNegocio() {
        this.repositorio = new RepositorioIDsArquivoBinario();
    }
    
    public static synchronized GeradorIDNegocio getInstancia() {
        if (instancia == null) {
            instancia = new GeradorIDNegocio();
        }
        return instancia;
    }
    
    public String gerarId(String prefixo) {
        return repositorio.gerarNovoId(prefixo);
    }
    
    // Métodos de constantes para expor os prefixos da classe GeradorID
    public String getPrefixoCliente() {
        return GeradorID.PREFIXO_CLIENTE;
    }
    
    public String getPrefixoSala() {
        return GeradorID.PREFIXO_SALA;
    }
    
    public String getPrefixoSessao() {
        return GeradorID.PREFIXO_SESSAO;
    }
    
    public String getPrefixoFilme() {
        return GeradorID.PREFIXO_FILME;
    }
    
    public String getPrefixoIngresso() {
        return GeradorID.PREFIXO_INGRESSO;
    }
} 