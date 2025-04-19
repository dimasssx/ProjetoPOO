package fachada;

import dados.*;
import negocio.entidades.*;
import negocio.*;
import negocio.exceptions.filmes.FilmeJaEstaNoCatalogoException;
import negocio.exceptions.filmes.FilmeNaoEstaCadastradoException;
import negocio.exceptions.filmes.NenhumFilmeEncontradoException;
import negocio.exceptions.salas.CodigoSalaJaExisteException;
import negocio.exceptions.salas.LimiteDeSalasExcedidoException;
import negocio.exceptions.salas.NenhumaSalaEncontradaException;
import negocio.exceptions.salas.SalaNaoEncontradaException;
import negocio.exceptions.sessoes.NenhumaSessaoEncontradaException;
import negocio.exceptions.sessoes.SessaoJaExisteException;
import negocio.exceptions.sessoes.SessaoNaoEncontradaException;

import java.time.LocalTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FachadaGerente {

    private FilmesNegocio filmesNegocio;
    private SalasNegocio salasNegocio;
    private SessoesNegocio sessoesNegocio;

    public FachadaGerente(){
        filmesNegocio = new FilmesNegocio(new RepositorioFilmesArquivoBinario());
        salasNegocio = new SalasNegocio(new RepositorioSalasArquivoBinario(),new RepositorioSessoesArquivoBinario());
        sessoesNegocio = new SessoesNegocio(new RepositorioSessoesArquivoBinario(),salasNegocio,filmesNegocio);
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

    public void removerFilme(String filme) throws FilmeNaoEstaCadastradoException {
        filmesNegocio.removerFilme(filme);
    }

    public void atualizarFilme(String nome,String genero,String duracao,String classificacao) throws FilmeNaoEstaCadastradoException {
        filmesNegocio.atualizarFilme(nome, genero, duracao, classificacao);
    }

    public Filme procurarFilme(String filme) throws FilmeNaoEstaCadastradoException {
        return filmesNegocio.procurarFilme(filme);
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

    public void adicionarSessao(String horario, String filme,String sala,String dia, double valorIngresso) throws SessaoJaExisteException, FilmeNaoEstaCadastradoException, SalaNaoEncontradaException {
        sessoesNegocio.adicionarSessao(horario,filme,sala,dia,valorIngresso);
    }

    public void removerSessao(String shorario,String sala,String sdia) throws SessaoNaoEncontradaException {
        MonthDay dia = MonthDay.parse(sdia, DateTimeFormatter.ofPattern("dd-MM"));
        LocalTime horario = LocalTime.parse(shorario);
        sessoesNegocio.removerSessao(horario,sala,dia);
    }

    public void atualizarSessao(String horario, String filme,String sala,String dia, double valorIngresso) throws SessaoNaoEncontradaException, FilmeNaoEstaCadastradoException, SalaNaoEncontradaException {
        sessoesNegocio.atualizarSessao(horario,filme,sala,dia,valorIngresso);
    }


    public ArrayList<String> procurarSessaoTitulo(String titulo) throws SessaoNaoEncontradaException {
        ArrayList<Sessao> sessoes = sessoesNegocio.procurarSessaoTitulo(titulo);
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

    public void removerSala(String codigo) throws SalaNaoEncontradaException {
        salasNegocio.removerSala(codigo);
    }

    public Sala procuraSala(String codigo) throws SalaNaoEncontradaException {
        return salasNegocio.procurarSala(codigo);
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
