package negocio;

import dados.IRepositorioIDs;
import dados.IRepositorioSessoes;

import java.time.LocalTime;
import java.time.MonthDay;
import java.util.ArrayList;

import dados.RepositorioIDsArquivoBinario;
import negocio.entidades.*;
import negocio.exceptions.assentos.AssentoIndisponivelException;
import negocio.exceptions.filmes.FilmeNaoEstaCadastradoException;
import negocio.exceptions.sessoes.*;
import negocio.exceptions.salas.SalaNaoEncontradaException;

public class SessoesNegocio {
    private final IRepositorioSessoes repositorioSessoes;
    private final SalasNegocio salasNegocio;
    private final FilmesNegocio filmesNegocio;
    private final IRepositorioIDs repositorioIDs;

    public SessoesNegocio(IRepositorioSessoes sessoes, SalasNegocio salasNegocio, FilmesNegocio filmesNegocio) {
        this.repositorioSessoes = sessoes;
        this.salasNegocio = salasNegocio;
        this.filmesNegocio = filmesNegocio;
        this.repositorioIDs = new RepositorioIDsArquivoBinario();
    }

    public void adicionarSessao(String horario, String sfilme, String ssala, String dia) throws SessaoJaExisteException, FilmeNaoEstaCadastradoException, SalaNaoEncontradaException, ConflitoHorarioException {

        Filme filme = filmesNegocio.procurarFilmePorID(sfilme);
        Sala sala = salasNegocio.procurarSala(ssala);
        Sessao sessao = new Sessao(filme, horario, sala, dia);
        verificarConflitoDeHorario(sessao,null);
        if (repositorioSessoes.existe(sessao)) {
            throw new SessaoJaExisteException();
        } else repositorioSessoes.adicionarSessao(sessao);
    }
    public void removerSessao(String id) throws SessaoNaoEncontradaException {
        Sessao sessaoprocurada = repositorioSessoes.procurarSessaoPorId(id);
        if (sessaoprocurada != null){
            repositorioSessoes.removerSessao(sessaoprocurada);
            repositorioIDs.removerID(GeradorID.getInstancia().getPrefixoSessao(), sessaoprocurada.getId());
        }
        else throw new SessaoNaoEncontradaException();
    }
    public void atualizarSessaoPorID(String id, LocalTime horario, MonthDay dia, String idFilme ) throws SessaoNaoEncontradaException, FilmeNaoEstaCadastradoException, ConflitoHorarioException {
        Sessao sessaoDesejada = repositorioSessoes.procurarSessaoPorId(id);
        if (sessaoDesejada == null) {
            throw new SessaoNaoEncontradaException();
        }
        Filme novoFilme = filmesNegocio.procurarFilmePorID(idFilme);
        sessaoDesejada.setFilme(novoFilme);
        sessaoDesejada.setDia(dia);
        sessaoDesejada.setHorario(horario);
        verificarConflitoDeHorario(sessaoDesejada,sessaoDesejada);
        repositorioSessoes.atualizarSessao(sessaoDesejada);
    }
    public Sessao procurarSessao(String id) throws SessaoNaoEncontradaException {
        Sessao sessaoprocurada = repositorioSessoes.procurarSessaoPorId(id);
        if (sessaoprocurada != null) return sessaoprocurada;
        else throw new SessaoNaoEncontradaException();
    }
    public ArrayList<Sessao> procurarSessaoTituloFilme(String titulo) throws SessaoNaoEncontradaException {
        ArrayList<Sessao> s = repositorioSessoes.procurarSessoesPorNomeDoFilme(titulo);
        if (s.isEmpty()) throw new SessaoNaoEncontradaException();
        else return s;
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
    private void verificarConflitoDeHorario(Sessao novaSessao, Sessao ignorarSessao) throws ConflitoHorarioException {
        LocalTime novoInicio = novaSessao.getHorario();
        int duracaoNovoFilme = converterDuracaoParaMinutos(novaSessao.getFilme().getDuracao());
        LocalTime novoFim = novoInicio.plusMinutes(duracaoNovoFilme);

        MonthDay dia = novaSessao.getDia();
        Sala sala = novaSessao.getSala();
        ArrayList<Sessao> sessoesDoDia = repositorioSessoes.buscarSessoesDoDia(dia);
        for (Sessao existente : sessoesDoDia) {
            if (!existente.getSala().getCodigo().equals(sala.getCodigo())) continue;
            if (existente.equals(ignorarSessao)) continue;
            LocalTime inicioExistente = existente.getHorario();
            int duracaoExistente = converterDuracaoParaMinutos(existente.getFilme().getDuracao());
            LocalTime fimExistente = inicioExistente.plusMinutes(duracaoExistente);

            boolean haConflito = !(novoFim.isBefore(inicioExistente) || novoInicio.isAfter(fimExistente));
            if (haConflito) {
                fimExistente = inicioExistente.plusMinutes(duracaoExistente+5);
                throw new ConflitoHorarioException(String.format(
                        "Conflito detectado na %s.\nSessão existente: %s - %s\nNova sessão tem %s",
                        sala.getCodigo(), inicioExistente, fimExistente, novaSessao.getFilme().getDuracao())
                );
            }
        }
    }
    private int converterDuracaoParaMinutos(String duracaoStr) {
        duracaoStr = duracaoStr.toLowerCase().replace(" ", "");
        int horas = 0, minutos = 0;
        if (duracaoStr.contains("h")) {
            String[] partes = duracaoStr.split("h");
            horas = Integer.parseInt(partes[0]);
            if (partes.length > 1 && !partes[1].isEmpty()) {
                minutos = Integer.parseInt(partes[1]);
            }
        }
        return horas * 60 + minutos;
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
    public void reservarSessaoInteira(String idSessao, int quantidadePessoas) throws AssentoIndisponivelException, SessaoNaoEncontradaException, SessaoIndisponivelParaReservaException {

        Sessao sessao = repositorioSessoes.procurarSessaoPorId(idSessao);
        if (sessao == null) {
            throw new SessaoNaoEncontradaException();
        }

        Assento[][] assentos = sessao.getAssentos();
        int fileiras = sessao.getSala().getFileiras();
        int assentosPorFileira = sessao.getSala().getAssentosPorFileira();
        int capacidadeTotal = fileiras * assentosPorFileira;

        if (quantidadePessoas > capacidadeTotal) {
            throw new SessaoIndisponivelParaReservaException();
        }

        for (int i = 0; i < fileiras; i++) {
            for (int j = 0; j < assentosPorFileira; j++) {
                if (!assentos[i][j].isReservado()) {
                    assentos[i][j].reservar();
                }
                else {
                    throw new SessaoIndisponivelParaReservaException();
                }
            }
        }

        repositorioSessoes.atualizarSessao(sessao);
    }
}
