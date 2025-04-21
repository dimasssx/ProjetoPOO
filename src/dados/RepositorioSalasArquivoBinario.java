package dados;

import java.io.*;
import java.util.ArrayList;

import negocio.entidades.Sala;

public class RepositorioSalasArquivoBinario implements IRepositorioSalas, Serializable {

    @Serial
    private static final long serialVersionUID = -4009776605163947716L;
    private ArrayList<Sala> salas;
    private File file;

    public RepositorioSalasArquivoBinario() {
        file = new File("salas.dat");
        if (file.exists()) {
            lerSalas();
        } else {
            salas = new ArrayList<Sala>();
            escritaSalas();
        }
    }

    // metodo para ler as salas do arquivo
    public void lerSalas() {
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            salas = (ArrayList<Sala>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            salas = new ArrayList<Sala>();
        }
    }

    // metodo para escrever as salas no arquivo
    public void escritaSalas() {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutput oos = new ObjectOutputStream(fos)) {
            oos.writeObject(salas);
        } catch (IOException e) {
            salas = new ArrayList<Sala>();
        }
    }

    // metodo para adicionar uma sala ao repositorio
    @Override
    public void adicionarSala(Sala sala) {
        salas.add(sala);
        escritaSalas();
    }

    // metodo para remover uma sala do repositorio a partir de um objeto de sala recebido
    @Override
    public void removerSala(Sala sala) {
        int index = salas.indexOf(sala);
        if (index != -1) {
            salas.remove(sala);
            escritaSalas();
        }
    }

    // metodo para remover uma sala do repositorio a partir do codigo da sala recebido
    @Override
    public void atualizarSala(Sala sala) {
        int index = salas.indexOf(sala);
        if (index != -1) {
            salas.set(index, sala);
            escritaSalas();
        }
    }

    // metodo para procurar uma sala no repositorio a partir do codigo da sala recebido
    @Override
    public Sala procurarSala(String id) {
        lerSalas();
        for(Sala s : salas){
            if(s.getId().equalsIgnoreCase(id)){
                return s;
            }
        }
        return null;
    }

    // metodo para listar todas as salas do repositorio
    @Override
    public ArrayList<Sala> listarSalas() {
        lerSalas();
        ArrayList<Sala> sala = new ArrayList<>();
        for (Sala s : salas) {
            if (s != null) {
                sala.add(s);
            }
        }
        return sala;
    }

    @Override
    public boolean existe(Sala sala) {
        lerSalas();
        return salas.contains(sala);
    }
}


