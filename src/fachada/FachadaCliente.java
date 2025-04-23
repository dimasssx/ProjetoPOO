package fachada;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import dados.*;
import negocio.*;
import negocio.entidades.*;
import negocio.entidades.pagamento.CartaoCredito;
import negocio.entidades.pagamento.CartaoDebito;
import negocio.entidades.pagamento.Pix;
import negocio.exceptions.sessoes.SessaoJaExibidaException;
import negocio.exceptions.filmes.FilmeNaoEstaCadastradoException;
import negocio.exceptions.sessoes.SessaoNaoEncontradaException;

public class FachadaCliente {

    private final ClientesNegocio clientesNegocio;
    private final FilmesNegocio filmeNegocio;
    private final SessoesNegocio sessoesNegocio;
    private final CardapioNegocio cardapioNegocio;

    public FachadaCliente() {
        this.clientesNegocio = new ClientesNegocio(new RepositorioClientesArquivoBinario());
        this.filmeNegocio = new FilmesNegocio(new RepositorioFilmesArquivoBinario(), new RepositorioSessoesArquivoBinario());
        this.sessoesNegocio = new SessoesNegocio(new RepositorioSessoesArquivoBinario(), new SalasNegocio(new RepositorioSalasArquivoBinario(), new RepositorioSessoesArquivoBinario()), filmeNegocio);
        this.cardapioNegocio = new CardapioNegocio();
    }
    //para testes
    public FachadaCliente(IRepositorioClientes repositorioClientes, IRepositorioFilmes repositorioFilmes, IRepositorioSessoes repositorioSessoes) {
        this.clientesNegocio = new ClientesNegocio(repositorioClientes);
        this.filmeNegocio = new FilmesNegocio(repositorioFilmes, repositorioSessoes);
        this.sessoesNegocio = new SessoesNegocio(repositorioSessoes, new SalasNegocio(new RepositorioSalasArquivoBinario(), repositorioSessoes), filmeNegocio);
        this.cardapioNegocio = new CardapioNegocio();
    }


    public ClientesNegocio getClienteNegocio() {
        return clientesNegocio;
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
    public Sessao procurarSessao(String id) throws SessaoNaoEncontradaException {
        return sessoesNegocio.procurarSessao(id);
    }
    public double calcularValorCompra(Cliente cliente, Sessao sessao, ArrayList<Ingresso> ingressos,ArrayList<Lanche> lanches) {
        return clientesNegocio.calcularValorTotalComTipos(cliente, sessao, ingressos,lanches);
    }
    public void adicionarIngresosAConta(Cliente cliente, ArrayList<Ingresso> ingressos) {
        clientesNegocio.adicionarIngressosAoCliente(cliente, ingressos);
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
    public void verificarSessaoAindaValida(Sessao sessao) throws SessaoJaExibidaException {
        LocalDate hoje = LocalDate.now();
        LocalDate dataSessao = sessao.getDia().atYear(hoje.getYear());
        LocalDateTime dataHoraSessao = LocalDateTime.of(dataSessao, sessao.getHorario());

        if (dataHoraSessao.isBefore(LocalDateTime.now())) {
            throw new SessaoJaExibidaException();
        }
    }
    public ArrayList<Lanche> listarCardapio(){
        return cardapioNegocio.obterCardapio();
    }
}

