package negocio;

import dados.RepositorioClientesArquivoBinario;
import negocio.entidades.Cliente;
import negocio.exceptions.usuario.ClienteNaoEncontradoException;

public class Autenticacao {
    private ClientesNegocio negocioCliente;

    public Autenticacao() {
        this.negocioCliente = new ClientesNegocio(new RepositorioClientesArquivoBinario());
    }

    public Cliente autenticar(String nomeDeUsuario, String senha) throws ClienteNaoEncontradoException {
        Cliente clientedesejado = negocioCliente.buscarCliente(nomeDeUsuario, senha);
        if (clientedesejado == null) throw new ClienteNaoEncontradoException();
        else return clientedesejado;
    }
}
