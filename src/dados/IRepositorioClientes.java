package dados;

import negocio.entidades.*;
import java.util.ArrayList;

public interface IRepositorioClientes {

    public void adicionarCliente(Cliente cliente);
    public Cliente retornarCliente(String login, String senha);
    public boolean validarCliente(String login, String senha);
    public ArrayList<Cliente> listarClientes();
    public void imprimir();
    public boolean existe(String login);
    public void removerCliente(Cliente cliente) ;
}