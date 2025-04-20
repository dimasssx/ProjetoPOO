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
    Sessao procurarSessaoPorId(String ID);
    ArrayList<Sessao> procurarSessaoporSala(String codigo);
    ArrayList<Sessao> procurarSessoesPorNomeDoFilme(String filme);
    ArrayList<Sessao> procurarSessoesPorIdDoFilme(String filme);
    ArrayList<Sessao> buscarSessoesDoDia(MonthDay dia);
    ArrayList<Sessao> retornarTodas();
    boolean existe(Sessao sessao);
}