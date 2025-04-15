package fachada;

import dados.*;
import negocio.entidades.*;
import negocio.exceptions.*;
import negocio.*;

import java.util.ArrayList;

public class FachadaGerente {

    private FilmesNegocio cadastroFilmes;
    private SalasNegocio cadastroSalas;
    private SessoesNegocio cadastroSessoes;

    public FachadaGerente(){
        cadastroFilmes = new FilmesNegocio(new RepositorioFilmesArquivoBinario());
        cadastroSalas = new SalasNegocio(new RepositorioSalas());
        cadastroSessoes = new SessoesNegocio(new RepositorioSessoes());
    }

    //operacoes de gerenciamento de filmes

    public void adicionarFilme(String nome,String genero,String duracao,String classificacao) throws FilmeJaEstaNoCatalogoException {
        cadastroFilmes.adicionarFilme(nome, genero, duracao, classificacao);
    }
    public void removerFilme(String filme) throws FilmeNaoEstaCadastradoException {
        cadastroFilmes.removerFilme(filme);
    }
    public void atualizarFilme(Filme filme) throws FilmeNaoEstaCadastradoException {
        cadastroFilmes.atualizarFilme(filme);
    }
    public Filme procurarFilme(String filme) throws FilmeNaoEstaCadastradoException {
        return cadastroFilmes.procurarFilme(filme);
    }
    public ArrayList<Filme> imprimirCatalogo() throws NenhumFilmeEncontradoException {
        return cadastroFilmes.listarCatalogo();
    }

    //Gerenciamento de sessoes

    public void adicionarSessao(Sessao sessao) throws SessaoJaExisteException {
        cadastroSessoes.adicionarSessao(sessao);
    }
    public void removerSessao(Sessao sessao) throws SessaoNaoEncontradaException {
        cadastroSessoes.removerSessao(sessao);
    }
    public void atualizarSessao(Sessao sessao) throws SessaoNaoEncontradaException {
        cadastroSessoes.atualizarSessao(sessao);
    }
    public Sessao procurarSessao(Sessao sessao) throws SessaoNaoEncontradaException{
        return cadastroSessoes.procurarSessao(sessao);
    }
    public ArrayList<Sessao> procurarSessaoporDia(String dia) throws SessaoNaoEncontradaException {
        return cadastroSessoes.procurarSessaodoDia(dia);
    }
    public ArrayList<Sessao> procurarSessaoTitulo(String titulo) throws SessaoNaoEncontradaException {
        return cadastroSessoes.procurarSessaoTitulo(titulo);
    }
    public ArrayList<Sessao> listarTodas() throws NenhumaSessaoEncontradaException{
        return cadastroSessoes.listarSessoes();
    }

    //gerenciamento de salas

    public void adicionarSala(Sala sala) throws CodigoSalaJaExisteException, LimiteDeSalasExcedidoException {
        cadastroSalas.adicionarSala(sala);
    }
    public void removerSala(String codigo) throws SalaNaoEncontradaException {
        cadastroSalas.removerSala(codigo);
    }
    public void atualizarSala(Sala sala) throws SalaNaoEncontradaException {
        cadastroSalas.atualizarSala(sala);
    }
    public Sala procuraSala(String codigo) throws SalaNaoEncontradaException {
        return cadastroSalas.procurarSala(codigo);
    }
    public ArrayList<Sala> listarSalas() throws NenhumaSalaEncontradaException {
        return cadastroSalas.listarSalas();
    }


}
