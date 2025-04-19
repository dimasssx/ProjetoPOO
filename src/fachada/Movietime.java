package fachada;

import dados.RepositorioClientesArquivoBinario;
import negocio.Autenticacao;
import negocio.ClienteNegocio;
import negocio.entidades.Cliente;
import negocio.exceptions.usuario.SenhaInvalidaException;
import negocio.exceptions.usuario.UsuarioJaExisteException;
import negocio.exceptions.usuario.ClienteNaoEncontradoException;

public class Movietime {
    private Autenticacao autenticacao;
    private ClienteNegocio negocioCliente;
    private FachadaCliente fachadaCliente;
    private FachadaGerente fachadaGerente;

    public Movietime(){
        this.autenticacao = new Autenticacao();
        this.negocioCliente = new ClienteNegocio(new RepositorioClientesArquivoBinario());
        this.fachadaCliente = new FachadaCliente();
        this.fachadaGerente = new FachadaGerente();
    }

    public FachadaGerente getFachadaGerente() {
        return fachadaGerente;
    }

    public FachadaCliente getFachadaCliente() {
        return fachadaCliente;
    }

    public Cliente autenticar(String nomeDeUsuario, String senha) throws ClienteNaoEncontradoException {
        return autenticacao.autenticar(nomeDeUsuario, senha);
    }

    public void cadastrarCliente(String nome, String nomeDeUsuario, String senha) throws UsuarioJaExisteException, SenhaInvalidaException {
        if(senha.length() < 8){
           throw new SenhaInvalidaException();
        }
        negocioCliente.adicionarCliente(nome, nomeDeUsuario, senha);
    }
}
