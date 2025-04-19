package dados;

import negocio.entidades.GeradorID;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class RepositorioIDsArquivoBinario implements IRepositorioIDs, Serializable {
    private static final long serialVersionUID = -4009776605163947718L;
    private static final String ARQUIVO_IDS = "ids_gerados.dat";
    private Map<String, Map<String, Boolean>> idsGerados;
    private File arquivo;
    
    public RepositorioIDsArquivoBinario() {
        arquivo = new File(ARQUIVO_IDS);
        if (arquivo.exists()) {
            lerIDs();
        } else {
            idsGerados = new HashMap<>();
            salvarIDs();
        }
    }
    
    private void lerIDs() {
        try (FileInputStream fis = new FileInputStream(arquivo);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            idsGerados = (Map<String, Map<String, Boolean>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            idsGerados = new HashMap<>();
        }
    }
    
    private void salvarIDs() {
        try (FileOutputStream fos = new FileOutputStream(arquivo);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(idsGerados);
        } catch (IOException e) {
            System.err.println("Erro ao salvar IDs: " + e.getMessage());
        }
    }
    
    @Override
    public boolean verificarIdExistente(String prefixo, String id) {
        lerIDs();
        if (!idsGerados.containsKey(prefixo)) {
            idsGerados.put(prefixo, new HashMap<>());
            return false;
        }
        return idsGerados.get(prefixo).containsKey(id);
    }
    
    @Override
    public void registrarId(String prefixo, String id) {
        lerIDs();
        if (!idsGerados.containsKey(prefixo)) {
            idsGerados.put(prefixo, new HashMap<>());
        }
        idsGerados.get(prefixo).put(id, true);
        salvarIDs();
    }
    
    @Override
    public String gerarNovoId(String prefixo) {
        String id;
        do {
            id = GeradorID.getInstancia().gerarId(prefixo);
        } while (verificarIdExistente(prefixo, id));
        
        registrarId(prefixo, id);
        return id;
    }
} 