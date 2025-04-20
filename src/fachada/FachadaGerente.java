package fachada;

import dados.*;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import negocio.*;
import negocio.entidades.*;
import negocio.exceptions.filmes.FilmeJaEstaNoCatalogoException;
import negocio.exceptions.filmes.FilmeNaoEstaCadastradoException;
import negocio.exceptions.filmes.NenhumFilmeEncontradoException;
import negocio.exceptions.sessoes.ValorInvalidoException;
import negocio.exceptions.salas.CodigoSalaJaExisteException;
import negocio.exceptions.salas.LimiteDeSalasExcedidoException;
import negocio.exceptions.salas.NenhumaSalaEncontradaException;
import negocio.exceptions.salas.SalaNaoEncontradaException;
import negocio.exceptions.sessoes.NenhumaSessaoEncontradaException;
import negocio.exceptions.sessoes.SessaoJaExisteException;
import negocio.exceptions.sessoes.SessaoNaoEncontradaException;

public class FachadaGerente {

    private IRepositorioFilmes repFilmes;
    private IRepositorioSalas repSalas;
    private IRepositorioSessoes repSessoes;
    private FilmesNegocio filmesNegocio;
    private SalasNegocio salasNegocio;
    private SessoesNegocio sessoesNegocio;

    public FachadaGerente() {
        this.repFilmes = new RepositorioFilmesArquivoBinario();
        this.repSalas = new RepositorioSalasArquivoBinario();
        this.repSessoes = new RepositorioSessoesArquivoBinario();
        this.filmesNegocio = new FilmesNegocio(repFilmes, repSessoes);
        this.salasNegocio = new SalasNegocio(repSalas, repSessoes);
        this.sessoesNegocio = new SessoesNegocio(repSessoes, salasNegocio, filmesNegocio);
    }

    public FilmesNegocio getFilmesNegocio() {
        return filmesNegocio;
    }

    public SalasNegocio getSalasNegocio() {
        return salasNegocio;
    }

    public SessoesNegocio getSessoesNegocio() {
        return sessoesNegocio;
    }

    //operacoes de gerenciamento de filmes

    public void adicionarFilme(String nome,String genero,String duracao,String classificacao) throws FilmeJaEstaNoCatalogoException {
        filmesNegocio.adicionarFilme(nome, genero, duracao, classificacao);
    }

    public void removerFilme(String ID) throws FilmeNaoEstaCadastradoException {
        filmesNegocio.removerFilme(ID);
    }

    public void atualizarFilmePorID(String id, String nome, String genero, String duracao, String classificacao) throws FilmeNaoEstaCadastradoException {
        filmesNegocio.atualizarFilmePorID(id, nome, genero, duracao, classificacao);
    }

    public Filme procurarFilmePorID(String ID) throws FilmeNaoEstaCadastradoException {
        return filmesNegocio.procurarFilmePorID(ID);
    }

    public Filme procurarFilmePorTitulo(String titulo) throws FilmeNaoEstaCadastradoException {
        return filmesNegocio.procurarFilmePorTitulo(titulo);
    }

    public ArrayList<String> imprimirCatalogo() throws NenhumFilmeEncontradoException {
        ArrayList<String> filmesFormatados = new ArrayList<>();
        ArrayList<Filme> filmesCatalogo = filmesNegocio.listarCatalogo();
        if (filmesCatalogo.isEmpty()) throw new NenhumFilmeEncontradoException();
        for (Filme filme:filmesCatalogo ){
            String filmeformatado = filme.toString();
            filmesFormatados.add(filmeformatado);
        }

        return filmesFormatados;
    }

    //Gerenciamento de sessoes

    public void adicionarSessao(String horario, String idFilme, String idSala, String dia, double valorIngresso) throws SessaoJaExisteException, FilmeNaoEstaCadastradoException, SalaNaoEncontradaException, ValorInvalidoException {
        sessoesNegocio.adicionarSessao(horario, idFilme, idSala, dia, valorIngresso);
    }

    public void removerSessao(String ID) throws SessaoNaoEncontradaException {
        sessoesNegocio.removerSessao(ID);
    }

//    public void atualizarSessao(String horario, String filme,String sala,String dia, double valorIngresso) throws SessaoNaoEncontradaException, FilmeNaoEstaCadastradoException, SalaNaoEncontradaException {
//        sessoesNegocio.atualizarSessao(horario,filme,sala,dia,valorIngresso);
//    }

    public ArrayList<String> procurarSessaoTitulo(String titulo) throws SessaoNaoEncontradaException {
        ArrayList<Sessao> sessoes = sessoesNegocio.procurarSessaoTituloFilme(titulo);
        ArrayList<String> formatadas = new ArrayList<>();
        for (Sessao s : sessoes) {
            formatadas.add(s.toString());
        }
        return formatadas;
    }

    public ArrayList<String> procurarSessaoporDia(String sdia) throws SessaoNaoEncontradaException {

        MonthDay dia = MonthDay.parse(sdia,DateTimeFormatter.ofPattern("dd-MM"));
        ArrayList<Sessao> sessoes = sessoesNegocio.procurarSessaodoDia(dia);
        ArrayList<String> formatadas = new ArrayList<>();

        for (Sessao s : sessoes) {
            formatadas.add(s.toString());
        }

        return formatadas;
    }

    public ArrayList<String> listarTodas() throws NenhumaSessaoEncontradaException {
        ArrayList<Sessao> sessoes = sessoesNegocio.listarSessoes();
        ArrayList<String> sessoesFormat = new ArrayList<>();

        for (Sessao s : sessoes){
            sessoesFormat.add(s.toString());
        }
        return sessoesFormat;
    }

    //gerenciamento de salas

    public void adicionarSala(String codigo,String tipo, int linhas, int colunas) throws CodigoSalaJaExisteException, LimiteDeSalasExcedidoException {
        salasNegocio.adicionarSala(codigo,tipo,linhas,colunas);
    }

    public void removerSala(String ID) throws SalaNaoEncontradaException {
        salasNegocio.removerSala(ID);
    }

    public Sala procurarSala(String ID) throws SalaNaoEncontradaException {
        return salasNegocio.procurarSala(ID);
    }

    public ArrayList<String> listarSalas() throws NenhumaSalaEncontradaException {
        ArrayList<String> salasformatadas = new ArrayList<>();
        ArrayList<Sala> salasarray = salasNegocio.listarSalas();
        if (salasarray.isEmpty()) throw new NenhumaSalaEncontradaException();
        for (Sala sala: salasarray ){
            String salaformt = sala.toString();
            salasformatadas.add(salaformt);
        }

        return salasformatadas;
    }
}
