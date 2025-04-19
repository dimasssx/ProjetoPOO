package negocio;

import dados.RepositorioClientesArquivoBinario;
import negocio.entidades.Cliente;
import negocio.exceptions.usuario.ClienteNaoEncontradoException;

public class Autenticacao {
    private ClienteNegocio negocioCliente;

    public Autenticacao() {
        this.negocioCliente = new ClienteNegocio(new RepositorioClientesArquivoBinario());
    }

    public boolean autenticarGerente(String nomeDeUsuario, String senha) {
        return nomeDeUsuario.equals("admin") && senha.equals("admin123");
    }

    public boolean autenticarCliente(String nomeDeUsuario, String senha) throws ClienteNaoEncontradoException {
        return negocioCliente.validarCliente(nomeDeUsuario, senha);
    }

    public Cliente autenticar(String nomeDeUsuario, String senha) throws ClienteNaoEncontradoException {
        Cliente clientedesejado = negocioCliente.buscarCliente(nomeDeUsuario, senha);
        if (clientedesejado == null) throw new ClienteNaoEncontradoException();
        else return clientedesejado;
    }
}
