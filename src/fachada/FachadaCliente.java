package fachada;

import dados.RepositorioClientesArquivoBinario;
import dados.RepositorioFilmesArquivoBinario;
import dados.RepositorioSalas;
import dados.RepositorioSessoes;
import negocio.ClienteNegocio;
import negocio.FilmesNegocio;
import negocio.SalasNegocio;
import negocio.SessoesNegocio;
import negocio.entidades.Cliente;
import negocio.entidades.Filme;
import negocio.entidades.Sessao;
import negocio.exceptions.*;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FachadaCliente {

    private ClienteNegocio clienteNegocio;
    private FilmesNegocio filmeNegocio;
    private SessoesNegocio sessoesNegocio;

    public FachadaCliente() {
        this.clienteNegocio = new ClienteNegocio(new RepositorioClientesArquivoBinario());
        this.filmeNegocio = new FilmesNegocio(new RepositorioFilmesArquivoBinario());
        this.sessoesNegocio = new SessoesNegocio(new RepositorioSessoes(),new SalasNegocio(new RepositorioSalas(),new RepositorioSessoes()), filmeNegocio);
    }

    //Visualizacao de filmes e sessoes

    public Filme consultarFilme(String nomeFilme) throws FilmeNaoEstaCadastradoException {
        return filmeNegocio.procurarFilme(nomeFilme);
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
        ArrayList<Sessao> sessoes = sessoesNegocio.procurarSessaoTitulo(titulo);
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

    public ArrayList<String> verCatalogo() throws NenhumFilmeEncontradoException {
        return filmeNegocio.filmesFormatados();
    }

    //Parte de compra de ingressos

    public void visuzalizarAssentosDaSessao(Sessao s) throws SessaoNaoEncontradaException {
        sessoesNegocio.mostrarAssentosDaSessao(s);
    }

    public void reservarAssento(Sessao s, String assento) throws AssentoIndisponivelException, SessaoNaoEncontradaException {
        int fileira = assento.charAt(0) - 'A';
        int poltrona = Integer.parseInt(assento.substring(1)) - 1;
        sessoesNegocio.reservarAssento(s, fileira, poltrona);
    }
}

