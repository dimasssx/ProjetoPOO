package dados;

import java.util.ArrayList;
import negocio.entidades.*;

public interface IRepositorioClientes {
    void adicionarCliente(Cliente cliente);
    Cliente retornarCliente(String nomeDeUsuario, String senha);
    ArrayList<Cliente> listarClientes();
    boolean existe(String nomeDeUsuario);
    void removerCliente(Cliente cliente);
    void atualizarCliente(Cliente cliente);
}