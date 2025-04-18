package negocio;

import dados.RepositorioClientesArquivoBinario;
import negocio.entidades.Cliente;
import negocio.entidades.ClienteVIP;
import negocio.exceptions.ClienteNaoEncontradoException;

public class Autenticacao {
    private ClienteNegocio negocioCliente;

    public Autenticacao() {
        this.negocioCliente = new ClienteNegocio(new RepositorioClientesArquivoBinario());
    }

    public boolean autenticarGerente(String login, String senha) {
        return login.equals("admin") && senha.equals("123");
    }
    public boolean autenticarCliente(String login, String senha) throws ClienteNaoEncontradoException {
        return negocioCliente.validarCliente(login, senha);
    }

    public Cliente autenticar(String login, String senha) throws ClienteNaoEncontradoException {
        Cliente clientedesejado = negocioCliente.buscarCliente(login,senha);
        if (clientedesejado == null) throw new ClienteNaoEncontradoException();
        else return clientedesejado;
    }
}
