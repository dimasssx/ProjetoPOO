package fachada;

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
import java.util.ArrayList;

public class FachadaCliente {

    private Cliente cliente;
    private ClienteNegocio clienteNegocio;
    private FilmesNegocio filmeNegocio;
    private SessoesNegocio sessoesNegocio;

    public FachadaCliente(Cliente cliente) {
        this.cliente = cliente;
        this.clienteNegocio = new ClienteNegocio();
        this.filmeNegocio = new FilmesNegocio(new RepositorioFilmesArquivoBinario());
        this.sessoesNegocio = new SessoesNegocio(new RepositorioSessoes(),new SalasNegocio(new RepositorioSalas()), filmeNegocio);
    }

    //Visualizacao de filmes e sessoes

    public Filme consultarFilme(String nomeFilme) throws FilmeNaoEstaCadastradoException {
        return filmeNegocio.procurarFilme(nomeFilme);
    }

    public ArrayList<String> acessarSessoesFormatadasPorDia(MonthDay dia) throws SessaoNaoEncontradaException {
        return sessoesNegocio.sessoesFormatadasPorDia(dia);
    }

    public ArrayList<String> acessarSessoesFormatadasPorFilmeEDia(String titulo, LocalDate dia) throws SessaoNaoEncontradaException {
        return sessoesNegocio.sessoesFormatadasPorFilmeEDia(titulo, dia);
    }

    public ArrayList<String> acessarSessoesFormatadasPorFilme(String titulo) throws SessaoNaoEncontradaException {
        return sessoesNegocio.sessoesFormatadasPorFilme(titulo);
    }

    public ArrayList<Sessao> procurarSessaoPorFilme(String titulo) throws SessaoNaoEncontradaException {
        return sessoesNegocio.procurarSessaoTitulo(titulo);
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

