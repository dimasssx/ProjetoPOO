package dados;

import java.time.LocalTime;
import java.time.MonthDay;
import java.util.ArrayList;

import negocio.entidades.Filme;
import negocio.entidades.Sessao;

public interface IRepositorioSessoes {

    void adicionarSessao(Sessao sessao);
    void removerSessao(Sessao sessao);
    void atualizarSessao(Sessao sessao);
    Sessao procurarSessao(Sessao sessao);
    Sessao procurarSessao(LocalTime horario, String sala, MonthDay dia);
    Sessao procurarSessao(LocalTime horario, Filme filme, MonthDay dia);
    ArrayList<Sessao> buscarSessoesDoDia(MonthDay dia);
    ArrayList<Sessao> procurarSessaoPorFilme(String filme);
    ArrayList<Sessao> retornarTodas();
    ArrayList<Sessao> procurarSessaoporSala(String codigo);
    boolean existe(Sessao sessao);

}