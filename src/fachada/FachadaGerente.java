package fachada;

import dados.*;
import negocio.entidades.*;
import negocio.exceptions.*;
import negocio.*;

import java.time.LocalTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FachadaGerente {

    private FilmesNegocio cadastroFilmes;
    private SalasNegocio cadastroSalas;
    private SessoesNegocio cadastroSessoes;

    public FachadaGerente(){
        cadastroFilmes = new FilmesNegocio(new RepositorioFilmesArquivoBinario());
        cadastroSalas = new SalasNegocio(new RepositorioSalasArquivoBinario(),new RepositorioSessoesArquivoBinario());
        cadastroSessoes = new SessoesNegocio(new RepositorioSessoesArquivoBinario(),cadastroSalas,cadastroFilmes);
    }

    public FilmesNegocio getCadastroFilmes() {
        return cadastroFilmes;
    }
    public SalasNegocio getCadastroSalas() {
        return cadastroSalas;
    }
    public SessoesNegocio getCadastroSessoes() {
        return cadastroSessoes;
    }

    //operacoes de gerenciamento de filmes

    public void adicionarFilme(String nome,String genero,String duracao,String classificacao) throws FilmeJaEstaNoCatalogoException {
        cadastroFilmes.adicionarFilme(nome, genero, duracao, classificacao);
    }
    public void removerFilme(String filme) throws FilmeNaoEstaCadastradoException {
        cadastroFilmes.removerFilme(filme);
    }
    public void atualizarFilme(String nome,String genero,String duracao,String classificacao) throws FilmeNaoEstaCadastradoException {
        cadastroFilmes.atualizarFilme(nome, genero, duracao, classificacao);
    }
    public Filme procurarFilme(String filme) throws FilmeNaoEstaCadastradoException {
        return cadastroFilmes.procurarFilme(filme);
    }
    public ArrayList<String> imprimirCatalogo() throws NenhumFilmeEncontradoException {
        ArrayList<String> filmesFormatados = new ArrayList<>();
        ArrayList<Filme> filmesCatalogo = cadastroFilmes.listarCatalogo();
        if (filmesCatalogo.isEmpty()) throw new NenhumFilmeEncontradoException();
        for (Filme filme:filmesCatalogo ){
            String filmeformatado = filme.toString();
            filmesFormatados.add(filmeformatado);
        }

        return filmesFormatados;
    }

    //Gerenciamento de sessoes

    public void adicionarSessao(String horario, String filme,String sala,String dia) throws SessaoJaExisteException, FilmeNaoEstaCadastradoException, SalaNaoEncontradaException {
        cadastroSessoes.adicionarSessao(horario,filme,sala,dia);
    }
    public void removerSessao(String shorario,String sala,String sdia) throws SessaoNaoEncontradaException {
        MonthDay dia = MonthDay.parse(sdia, DateTimeFormatter.ofPattern("dd-MM"));
        LocalTime horario = LocalTime.parse(shorario);
        cadastroSessoes.removerSessao(horario,sala,dia);
    }
    public void atualizarSessao(String horario, String filme,String sala,String dia) throws SessaoNaoEncontradaException, FilmeNaoEstaCadastradoException, SalaNaoEncontradaException {
        cadastroSessoes.atualizarSessao(horario,filme,sala,dia);
    }
    public String procurarSessao(String shorario,String sala,String sdia) throws SessaoNaoEncontradaException, SalaNaoEncontradaException {
        LocalTime horario = LocalTime.parse(shorario);
        MonthDay dia = MonthDay.parse(sdia,DateTimeFormatter.ofPattern("dd-MM"));
        return cadastroSessoes.procurarSessao(horario,sala,dia).toString();
    }

    public ArrayList<String> procurarSessaoTitulo(String titulo) throws SessaoNaoEncontradaException {
        ArrayList<Sessao> sessoes = cadastroSessoes.procurarSessaoTitulo(titulo);
        ArrayList<String> formatadas = new ArrayList<>();
        for (Sessao s : sessoes) {
            formatadas.add(s.toString());
        }
        return formatadas;
    }

    public ArrayList<String> procurarSessaoporDia(String sdia) throws SessaoNaoEncontradaException {

        MonthDay dia = MonthDay.parse(sdia,DateTimeFormatter.ofPattern("dd-MM"));
        ArrayList<Sessao> sessoes = cadastroSessoes.procurarSessaodoDia(dia);
        ArrayList<String> formatadas = new ArrayList<>();

        for (Sessao s : sessoes) {
            formatadas.add(s.toString());
        }

        return formatadas;
    }

    public ArrayList<String> listarTodas() throws NenhumaSessaoEncontradaException{
        ArrayList<Sessao> sessoes = cadastroSessoes.listarSessoes();
        ArrayList<String> sessoesFormat = new ArrayList<>();

        for (Sessao s : sessoes){
            sessoesFormat.add(s.toString());
        }
        return sessoesFormat;
    }

    //gerenciamento de salas

    public void adicionarSala(String codigo,String tipo, int linhas, int colunas) throws CodigoSalaJaExisteException, LimiteDeSalasExcedidoException {
        cadastroSalas.adicionarSala(codigo,tipo,linhas,colunas);
    }
    public void removerSala(String codigo) throws SalaNaoEncontradaException {
        cadastroSalas.removerSala(codigo);
    }

    public Sala procuraSala(String codigo) throws SalaNaoEncontradaException {
        return cadastroSalas.procurarSala(codigo);
    }
    public ArrayList<String> listarSalas() throws NenhumaSalaEncontradaException {
        ArrayList<String> salasformatadas = new ArrayList<>();
        ArrayList<Sala> salasarray = cadastroSalas.listarSalas();
        if (salasarray.isEmpty()) throw new NenhumaSalaEncontradaException();
        for (Sala sala: salasarray ){
            String salaformt = sala.toString();
            salasformatadas.add(salaformt);
        }

        return salasformatadas;
    }
}
