package dados;

import negocio.entidades.*;
import java.util.ArrayList;

public interface IRepositorioClientes {

    void adicionarCliente(Cliente cliente);
    Cliente retornarCliente(String login, String senha);
    boolean validarCliente(String login, String senha);
    ArrayList<Cliente> listarClientes();
    void imprimir();
    boolean existe(String login);
    void removerCliente(Cliente cliente);
    void atualizarCliente(Cliente cliente);
}