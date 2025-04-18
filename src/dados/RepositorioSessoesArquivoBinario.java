package dados;

import negocio.entidades.Filme;
import negocio.entidades.Sessao;
import java.io.*;
import java.time.LocalTime;
import java.time.MonthDay;
import java.util.ArrayList;


public class RepositorioSessoesArquivoBinario implements IRepositorioSessoes, Serializable {
    @Serial
    private static final long serialVersionUID = 5527816120194523955L;
    ArrayList<Sessao> sessoes;
    private File file;


    public RepositorioSessoesArquivoBinario() {
        file = new File("sessoes.dat");

        if (file.exists()) {
            lerSessoes();
        } else {
            sessoes = new ArrayList<>();
            escreverSessoes();
        }
    }

    public void lerSessoes() {
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            sessoes = (ArrayList<Sessao>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            sessoes = new ArrayList<>();
        }
    }

    public void escreverSessoes() {
        try (FileOutputStream fos = new FileOutputStream(file);
            ObjectOutput oos = new ObjectOutputStream(fos)) {
            oos.writeObject(sessoes);
        } catch (IOException e) {
            sessoes = new ArrayList<>();
        }
    }

    // metodo para adicionar uma sessao ao repositorio
    @Override
    public void adicionarSessao(Sessao sessao) {
        sessoes.add(sessao);
        escreverSessoes();
    }

    // metodo para remover uma sessao do repositorio a partir de um objeto de filme recebido
    @Override
    public void removerSessao(Sessao sessao) {
        int index = sessoes.indexOf(sessao);
        if (index != -1) {
            sessoes.remove(sessao);
            escreverSessoes();
        }
    }

    // metodo para atualizar uma sessao do repositorio a partir de um objeto de sessao recebido
    public void atualizarSessao(Sessao sessao) {
        int index = sessoes.indexOf(sessao);
        if (index != -1) {
            sessoes.set(index, sessao);
            escreverSessoes();
        }
    }

    // metodo para procurar uma sessao no repositorio a partir de um objeto de sessao recebido
    @Override
    public Sessao procurarSessao(Sessao sessao) {
        lerSessoes();
        Sessao sessaodesejada = null;
        for (Sessao s : sessoes) {
            if (s.equals(sessao)) {
                sessaodesejada = s;
            }
        }
        return sessaodesejada;

    }
    @Override
    public Sessao procurarSessao(LocalTime horario, String sala, MonthDay dia){
        lerSessoes();
        for (Sessao s : sessoes) {
            if (s.getHorario().equals(horario) &&
                    s.getSala().getCodigo().equals(sala) &&
                    s.getDia().equals(dia)) {
                return s;
            }
        }
        return null;
    }
    @Override
    public Sessao procurarSessao(LocalTime horario, Filme filme, MonthDay dia){
        lerSessoes();
        for (Sessao s : sessoes) {
            if (s.getHorario().equals(horario) &&
                    s.getFilme().equals(filme) &&
                    s.getDia().equals(dia)) {
                return s;
            }
        }
        return null;
    }
    @Override
    public ArrayList<Sessao> procurarSessaoporSala(String codigo){
        lerSessoes();
        ArrayList<Sessao> sessaoporSala = new ArrayList<>();
        for(Sessao s:sessoes){
            if(s.getSala().getCodigo().equalsIgnoreCase(codigo)){
                sessaoporSala.add(s);
            }
        }
        return sessaoporSala;
    }
    // metodo para procurar sessoes no repositorio a partir do nome do filme
    public ArrayList<Sessao> procurarSessaoPorFilme(String filme) {
        lerSessoes();
        ArrayList<Sessao> sessoesFilmes = new ArrayList<>();
        for (Sessao s : sessoes) {
            if (s.getFilme().getTitulo().equalsIgnoreCase(filme)) {
                sessoesFilmes.add(s);
            }
        }
        return sessoesFilmes;
    }

    // metodo para procurar sessoes no repositorio a partir do dia
    public ArrayList<Sessao> buscarSessoesDoDia(MonthDay dia) {
        lerSessoes();
        ArrayList<Sessao> sessoesDoDia = new ArrayList<Sessao>();
        for (Sessao s : sessoes) {
            if (s.getDia().equals(dia)) {
                sessoesDoDia.add(s);
            }
        }
        return sessoesDoDia;
    }
    // metodo para verificar se uma sessao existe no repositorio
    @Override
    public boolean existe(Sessao sessao) {
        lerSessoes();
        return sessoes.contains(sessao);
    }

    // metodo para retornar todas as sessoes do repositorio
    @Override
    public ArrayList<Sessao> retornarTodas() {
        lerSessoes();
        ArrayList<Sessao> sessoesFilmes = new ArrayList<>();
        for (Sessao s : sessoes) {
            if (s != null) {
                sessoesFilmes.add(s);
            }
        }
        return sessoesFilmes;
    }
}