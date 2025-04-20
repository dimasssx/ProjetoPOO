package dados;

public interface IRepositorioIDs {
    boolean verificarIdExistente(String prefixo, String id);
    void registrarId(String prefixo, String id);
} 