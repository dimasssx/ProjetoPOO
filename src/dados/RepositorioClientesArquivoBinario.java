package dados;

import java.io.*;
import java.util.ArrayList;
import negocio.entidades.Cliente;

public class RepositorioClientesArquivoBinario implements IRepositorioClientes, Serializable {

    @Serial
    private static final long serialVersionUID = -4009776605163947716L;
    private ArrayList<Cliente> clientes;
    private File file;

    public RepositorioClientesArquivoBinario() {
        file = new File("clientes.dat");
        if (file.exists()) {
            lerClientes();
        } else {
            clientes = new ArrayList<Cliente>();
            escritaClientes();
        }
    }

    public void lerClientes() {
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            clientes = (ArrayList<Cliente>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            clientes = new ArrayList<Cliente>();
        }
    }

    public void escritaClientes() {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutput oos = new ObjectOutputStream(fos)) {
            oos.writeObject(clientes);
        } catch (IOException e) {
            clientes = new ArrayList<Cliente>();
        }
    }

    @Override
    public void removerCliente(Cliente cliente) {
        int index = clientes.indexOf(cliente);
        if (index != -1) {
            clientes.remove(cliente);
            escritaClientes();
        }
    }


    @Override
    public void adicionarCliente(Cliente cliente) {
        clientes.add(cliente);
        escritaClientes();
    }
    //retornar o cliente a partir do seu nome de usuario e senha para o funcionamento das operacoes
    @Override
    public Cliente retornarCliente(String nomeDeUsuario, String senha) {
        lerClientes();
        for (Cliente c : clientes) {
            if (c.getNomeDeUsuario().equals(nomeDeUsuario) && c.getSenha().equals(senha)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public void atualizarCliente(Cliente clienteAtualizado) {
        // Garantir que os dados estejam atualizados antes de modificar
        lerClientes();
        
        for (int i = 0; i < clientes.size(); i++) {
            Cliente c = clientes.get(i);
            if (c.getNomeDeUsuario().equals(clienteAtualizado.getNomeDeUsuario())) {
                clientes.set(i, clienteAtualizado); // substitui o cliente antigo
                escritaClientes(); // persiste no arquivo
                return;
            }
        }
        
        // Se não encontrou o cliente, adiciona ele
        clientes.add(clienteAtualizado);
        escritaClientes();
    }

    //retornar todos os clientes cadastrados
    @Override
    public ArrayList<Cliente> listarClientes() {
        lerClientes();
        return new ArrayList<>(clientes);
    }

    //verifica se o cliente ja existe no sistema, a partir do seu nome de usuario
    @Override
    public boolean existe(String nomeDeUsuario) {
        lerClientes();
        for (Cliente cliente : clientes) {
            if (cliente.getNomeDeUsuario().equals(nomeDeUsuario)) {
                return true;
            }
        }
        return false;
    }
}