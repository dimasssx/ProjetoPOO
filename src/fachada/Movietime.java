package fachada;

import dados.IRepositorioClientes;
import dados.RepositorioClientesArquivoBinario;
import negocio.Autenticacao;
import negocio.ClienteNegocio;
import negocio.entidades.Cliente;
import negocio.exceptions.ClienteJaExisteException;
import negocio.exceptions.ClienteNaoEncontradoException;

public class Movietime {
    private Autenticacao autenticacao;
    private ClienteNegocio negocioCliente;
    private FachadaCliente fachadaCliente;
    //private FachadaClienteVIP fachadaClienteVIP;
    private FachadaGerente fachadaGerente;

    public Movietime(){
        this.autenticacao = new Autenticacao();
        this.negocioCliente = new ClienteNegocio(new RepositorioClientesArquivoBinario());
        this.fachadaCliente = new FachadaCliente();
        this.fachadaGerente = new FachadaGerente();
        //this.fachadaClienteVIP = new FachadaClienteVIP();

    }
    public int autenticar(String login, String senha) throws ClienteNaoEncontradoException {
        return autenticacao.autenticar(login, senha);
    }
    public void cadastrarCliente(String nome, String login, String senha) throws ClienteJaExisteException {
        negocioCliente.adicionarCliente(nome, login, senha);
    }
    public FachadaGerente getFachadaGerente() {
        return fachadaGerente;
    }
    public FachadaCliente getFachadaCliente() {
        return fachadaCliente;
    }
}
