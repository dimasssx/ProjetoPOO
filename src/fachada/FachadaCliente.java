package fachada;

import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import dados.RepositorioClientesArquivoBinario;
import dados.RepositorioFilmesArquivoBinario;
import dados.RepositorioSalasArquivoBinario;
import dados.RepositorioSessoesArquivoBinario;
import negocio.ClientesNegocio;
import negocio.FilmesNegocio;
import negocio.SalasNegocio;
import negocio.SessoesNegocio;
import negocio.entidades.Filme;
import negocio.entidades.Sessao;
import negocio.entidades.Cliente;
import negocio.entidades.pagamento.CartaoCredito;
import negocio.entidades.pagamento.CartaoDebito;
import negocio.entidades.pagamento.MetodoPagamento;
import negocio.entidades.pagamento.Pix;
import negocio.exceptions.filmes.FilmeNaoEstaCadastradoException;
import negocio.exceptions.sessoes.SessaoNaoEncontradaException;

public class FachadaCliente {

    private ClientesNegocio clientesNegocio;
    private FilmesNegocio filmeNegocio;
    private SessoesNegocio sessoesNegocio;

    public FachadaCliente() {
        this.clientesNegocio = new ClientesNegocio(new RepositorioClientesArquivoBinario());
        this.filmeNegocio = new FilmesNegocio(new RepositorioFilmesArquivoBinario(), new RepositorioSessoesArquivoBinario());
        this.sessoesNegocio = new SessoesNegocio(new RepositorioSessoesArquivoBinario(), new SalasNegocio(new RepositorioSalasArquivoBinario(), new RepositorioSessoesArquivoBinario()), filmeNegocio);
    }

    public ClientesNegocio getClienteNegocio() {
        return clientesNegocio;
    }

    public FilmesNegocio getFilmeNegocio() {
        return filmeNegocio;
    }

    public SessoesNegocio getSessoesNegocio() {
        return sessoesNegocio;
    }

    //Visualizacao de filmes e sessoes

    public Filme consultarFilme(String nomeFilme) throws FilmeNaoEstaCadastradoException {
        return filmeNegocio.procurarFilmePorTitulo(nomeFilme);
    }

    public ArrayList<String> procurarSessaoporDia(String sdia) throws SessaoNaoEncontradaException {

        MonthDay dia = MonthDay.parse(sdia, DateTimeFormatter.ofPattern("dd-MM"));
        ArrayList<Sessao> sessoes = sessoesNegocio.procurarSessaodoDia(dia);
        ArrayList<String> formatadas = new ArrayList<>();

        for (Sessao s : sessoes) {
            formatadas.add(s.toString());
        }

        return formatadas;
    }

    public ArrayList<String> procurarSessaoPorTituloDoFilme(String titulo) throws SessaoNaoEncontradaException {
        ArrayList<Sessao> sessoes = sessoesNegocio.procurarSessaoTituloFilme(titulo);
        ArrayList<String> formatadas = new ArrayList<>();
        for (Sessao s : sessoes) {
            formatadas.add(s.toString());
        }
        return formatadas;
    }

    public ArrayList<String> procurarSessoesHoje() throws SessaoNaoEncontradaException {
        MonthDay hoje = MonthDay.now();
        ArrayList<Sessao> sessoes = sessoesNegocio.procurarSessaodoDia(hoje);
        ArrayList<String> formatadas = new ArrayList<>();

        for (Sessao s : sessoes) {
            formatadas.add(s.toString());
        }

        return formatadas;
    }

    public Sessao procurarSessao(String ID) throws SessaoNaoEncontradaException {
        return sessoesNegocio.procurarSessao(ID);
    }

    public String pagarComPIX(double valor){
        Pix pix = new Pix();
        return pix.pagar(valor);
    }

    public String pagarComDebito(String numero, String titularCartao, double valor){
        CartaoDebito debito = new CartaoDebito(numero, titularCartao);
        return debito.pagar(valor);
    }

    public String pagarComCredito(String numero, String titularCartao, double valor){
        CartaoCredito credito = new CartaoCredito(numero, titularCartao);
        return credito.pagar(valor);
    }

    public void alterarSenha(Cliente cliente, String novaSenha) {
        clientesNegocio.alterarSenha(cliente, novaSenha);
    }

}

