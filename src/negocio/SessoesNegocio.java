package negocio;

import dados.IRepositorioSessoes;
import negocio.entidades.Assento;
import negocio.entidades.Sessao;
import negocio.exceptions.*;

import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SessoesNegocio {
    IRepositorioSessoes sessoes;

    public SessoesNegocio(IRepositorioSessoes sessoes) {
        this.sessoes = sessoes;
    }

    public void adicionarSessao(Sessao sessao) throws SessaoJaExisteException {
        if (sessoes.existe(sessao)) {
            throw new SessaoJaExisteException();
        } else sessoes.adicionarSessao(sessao);
    }

    public void removerSessao(Sessao sessao) throws SessaoNaoEncontradaException {
        Sessao sessaoprocurada = sessoes.procurarSessao(sessao);
        if (sessaoprocurada != null) sessoes.removerSessao(sessaoprocurada);
        else throw new SessaoNaoEncontradaException();

    }

    public void atualizarSessao(Sessao sessao) throws SessaoNaoEncontradaException {
        Sessao s = sessoes.procurarSessao(sessao);
        if (s != null) sessoes.atualizarSessao(sessao);
        else throw new SessaoNaoEncontradaException();

    }

    public Sessao procurarSessao(Sessao sessao) throws SessaoNaoEncontradaException {
        Sessao sessaoprocurada = sessoes.procurarSessao(sessao);
        if (sessaoprocurada != null) return sessaoprocurada;
        else throw new SessaoNaoEncontradaException();

    }

    public ArrayList<Sessao> procurarSessaoTitulo(String titulo) throws SessaoNaoEncontradaException {
        ArrayList<Sessao> s = sessoes.procurarSessao(titulo);
        if (s != null) return s;
        else throw new SessaoNaoEncontradaException();
    }

    public ArrayList<Sessao> procurarSessaodoDia(String sdia) throws SessaoNaoEncontradaException {
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MM");
        MonthDay dia = MonthDay.parse(sdia, formater);
        ArrayList<Sessao> s = sessoes.buscarSessoesDoDia(dia);
        if (s != null) return s;
        else throw new SessaoNaoEncontradaException();
    }

    public void mostrarAssentosDaSessao(Sessao sessao) throws SessaoNaoEncontradaException {
        Sessao s = sessoes.procurarSessao(sessao);
        if (s != null) {
            sessao.mostrarAssentos();
        } else {
            throw new SessaoNaoEncontradaException();
        }
    }

    public void reservarAssento(Sessao sessao, int fileira, int poltrona) throws AssentoIndisponivelException {
        Sessao s = sessoes.procurarSessao(sessao);
        if (s != null) {
            Assento a = s.getAssento(fileira, poltrona);
            if (!a.isReservado()) {
                s.reservarAssento(fileira, poltrona);
            } else {
                throw new AssentoIndisponivelException();
            }
        }
    }


    public ArrayList<Sessao> listarSessoes() throws NenhumaSessaoEncontradaException {
        if (sessoes.retornarTodas().isEmpty()) {
            throw new NenhumaSessaoEncontradaException();
        } else return sessoes.retornarTodas();
    }
}
