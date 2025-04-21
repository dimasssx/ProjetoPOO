package dados;

import java.time.MonthDay;
import java.util.ArrayList;
import negocio.entidades.Sessao;

public interface IRepositorioSessoes {
    void adicionarSessao(Sessao sessao);
    void removerSessao(Sessao sessao);
    void atualizarSessao(Sessao sessao);
    Sessao procurarSessao(Sessao sessao);
    Sessao procurarSessaoPorId(String id);
    ArrayList<Sessao> procurarSessoesPorIdSala(String codigo);
    ArrayList<Sessao> procurarSessoesPorNomeDoFilme(String nome);
    ArrayList<Sessao> procurarSessoesPorIdDoFilme(String idFilme);
    ArrayList<Sessao> buscarSessoesDoDia(MonthDay dia);
    ArrayList<Sessao> retornarTodas();
    boolean existe(Sessao sessao);
}