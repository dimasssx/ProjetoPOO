package negocio;

import dados.RepositorioClientesArquivoBinario;
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

    public int autenticar(String login, String senha) throws ClienteNaoEncontradoException {
        if (login.equals("admin") && senha.equals("123")) {
            return 1; // Gerente
        } else if (negocioCliente.validarCliente(login, senha)) {
            if(negocioCliente.buscarCliente(login, senha) instanceof ClienteVIP) {
                return 2; // Cliente VIP
            } else {
                return 3; // Cliente Comum
            }
        }
        throw new ClienteNaoEncontradoException();
    }
}
