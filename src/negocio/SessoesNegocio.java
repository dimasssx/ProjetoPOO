package negocio;

import java.time.LocalTime;
import java.time.MonthDay;
import java.util.ArrayList;

import dados.IRepositorioSessoes;
import negocio.entidades.Assento;
import negocio.entidades.Filme;
import negocio.entidades.Sala;
import negocio.entidades.Sessao;
import negocio.exceptions.assentos.AssentoIndisponivelException;
import negocio.exceptions.filmes.FilmeNaoEstaCadastradoException;
import negocio.exceptions.salas.SalaNaoEncontradaException;
import negocio.exceptions.sessoes.NenhumaSessaoEncontradaException;
import negocio.exceptions.sessoes.SessaoJaExisteException;
import negocio.exceptions.sessoes.SessaoNaoEncontradaException;

public class SessoesNegocio {
    private IRepositorioSessoes repositorioSessoes;
    private SalasNegocio salasNegocio;
    private FilmesNegocio filmesNegocio;

    public SessoesNegocio(IRepositorioSessoes sessoes,SalasNegocio salasNegocio,FilmesNegocio filmesNegocio) {
        this.repositorioSessoes = sessoes;
        this.salasNegocio = salasNegocio;
        this.filmesNegocio = filmesNegocio;
    }

    public void adicionarSessao(String horario, String sfilme, String ssala, String dia, double valorIngresso) throws SessaoJaExisteException, FilmeNaoEstaCadastradoException, SalaNaoEncontradaException {
        Filme filme = filmesNegocio.procurarFilme(sfilme);
        Sala sala = salasNegocio.procurarSala(ssala);
        Sessao sessao = new Sessao(filme, horario, sala, dia, valorIngresso);
        if (repositorioSessoes.existe(sessao)) {
            throw new SessaoJaExisteException();
        } else repositorioSessoes.adicionarSessao(sessao);
    }

    public void removerSessao(LocalTime horario,String sala,MonthDay dia) throws SessaoNaoEncontradaException {
        Sessao sessaoprocurada = repositorioSessoes.procurarSessao(horario, sala, dia);
        if (sessaoprocurada != null) repositorioSessoes.removerSessao(sessaoprocurada);
        else throw new SessaoNaoEncontradaException();

    }

    public void atualizarSessao(String horario, String sfilme, String ssala, String dia, double valorIngresso) throws SessaoNaoEncontradaException, FilmeNaoEstaCadastradoException, SalaNaoEncontradaException {
        Filme filme = filmesNegocio.procurarFilme(sfilme);
        Sala sala = salasNegocio.procurarSala(ssala);
        Sessao s = new Sessao(filme,horario,sala,dia, valorIngresso);
        if (repositorioSessoes.existe(s)) repositorioSessoes.atualizarSessao(s);
        else throw new SessaoNaoEncontradaException();
    }

    public Sessao procurarSessao(LocalTime horario, String filme, MonthDay dia) throws SessaoNaoEncontradaException, FilmeNaoEstaCadastradoException {
        Filme ofilme = filmesNegocio.procurarFilme(filme);
        Sessao sessaoprocurada = repositorioSessoes.procurarSessao(horario,ofilme, dia);
        if (sessaoprocurada != null) return sessaoprocurada;
        else throw new SessaoNaoEncontradaException();

    }

    public ArrayList<Sessao> procurarSessaoTitulo(String titulo) throws SessaoNaoEncontradaException {
        ArrayList<Sessao> s = repositorioSessoes.procurarSessaoPorFilme(titulo);
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

    public void mostrarAssentosDaSessao(Sessao sessao) throws SessaoNaoEncontradaException {
        Sessao s = repositorioSessoes.procurarSessao(sessao);
        if (s != null) {
            mostrarAssentos(s);
        } else {
            throw new SessaoNaoEncontradaException();
        }
    }

    public void mostrarAssentos(Sessao sessao) {
        Assento[][] assentos = sessao.getAssentos();
        System.out.println("Mapa de assentos - " + sessao.getFilme().getTitulo() + " às " + sessao.getHorario() + " (" + sessao.getDiaFormatado() + ")");
        for (int i = 0; i < assentos.length; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < assentos[i].length; j++) {
                if (assentos[i][j].isReservado()) {
                    System.out.print("[X] ");
                } else {
                    System.out.print("[ ] ");
                }
            }
            System.out.println();
        }
        System.out.print("   ");
        for (int j = 0; j < assentos[0].length; j++) {
            System.out.print((j + 1) + "   ");
        }
        System.out.println("\nLegenda: [ ] disponível | [X] reservado");
    }

    //utiliza o metodo marcarAssentoReserrvado para reservar um assento, fazendo algumas outras verificações
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

    private void marcarAssentoComoReservado(Sessao sessao, int fileira, int numero) throws AssentoIndisponivelException {
        Assento[][] assentos = sessao.getAssentos();
        if (fileira >= 0 && fileira < assentos.length && numero >= 0 && numero < assentos[0].length) {
            if (!assentos[fileira][numero].isReservado()) {
                assentos[fileira][numero].reservar(); // Marca como reservado
            } else {
                throw new AssentoIndisponivelException();
            }
        }
    }
}
