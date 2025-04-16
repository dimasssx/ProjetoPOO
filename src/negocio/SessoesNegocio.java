package negocio;

import dados.IRepositorioSessoes;
import negocio.entidades.Assento;
import negocio.entidades.Sessao;
import negocio.exceptions.*;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SessoesNegocio {
    private IRepositorioSessoes sessoes;

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

    public ArrayList<Sessao> listarSessoes() throws NenhumaSessaoEncontradaException {
        if (sessoes.retornarTodas().isEmpty()) {
            throw new NenhumaSessaoEncontradaException();
        } else return sessoes.retornarTodas();
    }

    public Sessao procurarSessao(Sessao sessao) throws SessaoNaoEncontradaException {
        Sessao sessaoprocurada = sessoes.procurarSessao(sessao);
        if (sessaoprocurada != null) return sessaoprocurada;
        else throw new SessaoNaoEncontradaException();

    }

    public ArrayList<Sessao> procurarSessaoTitulo(String titulo) throws SessaoNaoEncontradaException {
        ArrayList<Sessao> s = sessoes.procurarSessaoPorFilme(titulo);
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

    public void reservarAssento(Sessao sessao, int fileira, int poltrona) throws AssentoIndisponivelException, SessaoNaoEncontradaException {
        Sessao s = sessoes.procurarSessao(sessao);
        if (s != null) {
            Assento a = s.getAssento(fileira, poltrona);
            if (a != null && !a.isReservado()) {
                marcarAssentoComoReservado(s, fileira, poltrona);
                sessoes.atualizarSessao(s);
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
    
    public int assentosDisponiveis(Sessao sessao) throws SessaoNaoEncontradaException {
        Sessao s = sessoes.procurarSessao(sessao);
        if (s != null) {
            return contarAssentosDisponiveis(s);
        } else {
            throw new SessaoNaoEncontradaException();
        }
    }
    
    private int contarAssentosDisponiveis(Sessao sessao) {
        Assento[][] assentos = sessao.getAssentos();
        int disponiveis = 0;
        for (int i = 0; i < assentos.length; i++) {
            for (int j = 0; j < assentos[i].length; j++) {
                if (!assentos[i][j].isReservado()) {
                    disponiveis++;
                }
            }
        }
        return disponiveis;
    }
}
