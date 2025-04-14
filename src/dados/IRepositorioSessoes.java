package dados;

import java.time.LocalTime;
import java.time.MonthDay;
import java.util.ArrayList;

import negocio.entidades.Sessao;

public interface IRepositorioSessoes {
    void adicionarSessao(Sessao sessao);
    void removerSessao(Sessao sessao);
    void atualizarSessao(Sessao sessao);
    ArrayList<Sessao> buscarSessoesDoDia(MonthDay dia);
    boolean existe(Sessao sessao);
    void imprimir();
    Sessao procurarSessao(Sessao sessao);
    public Sessao procurarSessao(MonthDay dia, String filme, LocalTime horario);
    ArrayList<Sessao> procurarSessao(String filme);
    ArrayList<Sessao> retornarTodas();
    void removerSessao(LocalTime horario, String codigoSala, MonthDay dia);
}