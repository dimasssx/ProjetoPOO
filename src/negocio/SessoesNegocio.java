package negocio;

import dados.IRepositorioSessoes;
import java.time.MonthDay;
import java.util.ArrayList;
import negocio.entidades.Assento;
import negocio.entidades.Filme;
import negocio.entidades.Sala;
import negocio.entidades.Sessao;
import negocio.exceptions.assentos.AssentoIndisponivelException;
import negocio.exceptions.filmes.FilmeNaoEstaCadastradoException;
import negocio.exceptions.sessoes.ValorInvalidoException;
import negocio.exceptions.salas.SalaNaoEncontradaException;
import negocio.exceptions.sessoes.NenhumaSessaoEncontradaException;
import negocio.exceptions.sessoes.SessaoJaExisteException;
import negocio.exceptions.sessoes.SessaoNaoEncontradaException;

public class SessoesNegocio {
    private IRepositorioSessoes repositorioSessoes;
    private SalasNegocio salasNegocio;
    private FilmesNegocio filmesNegocio;

    public SessoesNegocio(IRepositorioSessoes sessoes, SalasNegocio salasNegocio, FilmesNegocio filmesNegocio) {
        this.repositorioSessoes = sessoes;
        this.salasNegocio = salasNegocio;
        this.filmesNegocio = filmesNegocio;
    }

    public void adicionarSessao(String horario, String sfilme, String ssala, String dia, double valorIngresso) throws SessaoJaExisteException, FilmeNaoEstaCadastradoException, SalaNaoEncontradaException, ValorInvalidoException {
        if (valorIngresso <= 0) {
            throw new ValorInvalidoException();
        }
        
        Filme filme = filmesNegocio.procurarFilmePorID(sfilme);
        Sala sala = salasNegocio.procurarSala(ssala);
        Sessao sessao = new Sessao(filme, horario, sala, dia, valorIngresso);
        if (repositorioSessoes.existe(sessao)) {
            throw new SessaoJaExisteException();
        } else repositorioSessoes.adicionarSessao(sessao);
    }

    public void removerSessao(String ID) throws SessaoNaoEncontradaException {
        Sessao sessaoprocurada = repositorioSessoes.procurarSessaoPorId(ID);
        if (sessaoprocurada != null) repositorioSessoes.removerSessao(sessaoprocurada);
        else throw new SessaoNaoEncontradaException();

    }

//    public void atualizarSessao(String horario, String idFilme, String idSala, String dia, double valorIngresso) throws SessaoNaoEncontradaException, FilmeNaoEstaCadastradoException, SalaNaoEncontradaException {
//        Filme filme = filmesNegocio.procurarFilmePorID(idFilme);
//        Sala sala = salasNegocio.procurarSala(idSala);
//        Sessao s = new Sessao(filme,horario,sala,dia, valorIngresso);
//        if (repositorioSessoes.existe(s)) repositorioSessoes.atualizarSessao(s);
//        else throw new SessaoNaoEncontradaException();
//    }
//
//    public void atualizarSessaoPorID(String id, String idFilme, String idSala, String dia, String horario) throws SessaoNaoEncontradaException, FilmeNaoEstaCadastradoException, SalaNaoEncontradaException {
//        Sessao sessaoDesejada = repositorioSessoes.procurarSessaoPorId(id);
//
//        if (sessaoDesejada == null) {
//            throw new SessaoNaoEncontradaException();
//        }
//        Filme novoFilme = filmesNegocio.procurarFilmePorID(idFilme);
//        sessaoDesejada.setFilme(novoFilme);
//        Sala novaSala = salasNegocio.procurarSala(idSala);
//        sessaoDesejada.setSala(novaSala);
//        sessaoDesejada.setDia(dia);
//        filmeExistente.setClassificacao(classificacao);
//
//        repositorioFilmes.atualizaFilme(filmeExistente);
//    }

    public Sessao procurarSessao(String ID) throws SessaoNaoEncontradaException {
        Sessao sessaoprocurada = repositorioSessoes.procurarSessaoPorId(ID);
        if (sessaoprocurada != null) return sessaoprocurada;
        else throw new SessaoNaoEncontradaException();
    }

    public ArrayList<Sessao> procurarSessaoTituloFilme(String titulo) throws SessaoNaoEncontradaException {
        ArrayList<Sessao> s = repositorioSessoes.procurarSessoesPorNomeDoFilme(titulo);
        if (s != null) return s;
        else throw new SessaoNaoEncontradaException();
    }

    public ArrayList<Sessao> procurarSessaodoDia(MonthDay dia) throws SessaoNaoEncontradaException {
        ArrayList<Sessao> s = repositorioSessoes.buscarSessoesDoDia(dia);
        if (s != null) return s;
        else throw new SessaoNaoEncontradaException();
    }

    public ArrayList<Sessao> listarSessoes() throws NenhumaSessaoEncontradaException {
        if (repositorioSessoes.retornarTodas().isEmpty()) {
            throw new NenhumaSessaoEncontradaException();
        } else return repositorioSessoes.retornarTodas();
    }

    //utiliza o metodo marcarAssentoReservado para reservar um assento, fazendo algumas outras verificações
    public void reservarAssento(Sessao sessao, int fileira, int poltrona) throws AssentoIndisponivelException, SessaoNaoEncontradaException {
        Sessao s = repositorioSessoes.procurarSessao(sessao);
        if (s != null) {
            Assento a = s.getAssento(fileira, poltrona);
            if (a != null && !a.isReservado()) {
                marcarAssentoComoReservado(s, fileira, poltrona);
                repositorioSessoes.atualizarSessao(s);
            } else {
                throw new AssentoIndisponivelException();
            }
        } else {
            throw new SessaoNaoEncontradaException();
        }
    }

    private void marcarAssentoComoReservado(Sessao sessao, int fileira, int poltrona) throws AssentoIndisponivelException {
        Assento[][] assentos = sessao.getAssentos();
        if (fileira >= 0 && fileira < assentos.length && poltrona >= 0 && poltrona < assentos[0].length) {
            if (!assentos[fileira][poltrona].isReservado()) {
                assentos[fileira][poltrona].reservar(); // Marca como reservado
            } else {
                throw new AssentoIndisponivelException();
            }
        }
    }
}
