package dados;

import negocio.entidades.Sessao;
import java.io.*;
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
    public RepositorioSessoesArquivoBinario(String caminho){
        file = new File(caminho);
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
    @Override
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
    public Sessao procurarSessaoPorId(String id){
        lerSessoes();
        for (Sessao s : sessoes) {
            if (s.getId().equalsIgnoreCase(id)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Sessao> procurarSessoesPorIdSala(String id){
        lerSessoes();
        ArrayList<Sessao> sessoesDaSala = new ArrayList<>();
        for(Sessao s : sessoes){
            if(s.getSala().getId().equalsIgnoreCase(id)){
                sessoesDaSala.add(s);
            }
        }
        return sessoesDaSala;
    }

    // metodo para procurar sessoes no repositorio a partir do nome do filme
    @Override
    public ArrayList<Sessao> procurarSessoesPorNomeDoFilme(String filme) {
        lerSessoes();
        ArrayList<Sessao> sessoesFilmes = new ArrayList<>();
        for (Sessao s : sessoes) {
            if (s.getFilme().getTitulo().equalsIgnoreCase(filme)) {
                sessoesFilmes.add(s);
            }
        }
        return sessoesFilmes;
    }

    public ArrayList<Sessao> procurarSessoesPorIdDoFilme(String filme) {
        lerSessoes();
        ArrayList<Sessao> sessoesDoFilme = new ArrayList<>();
        for (Sessao s : sessoes) {
            if (s.getFilme().getId().equalsIgnoreCase(filme)) {
                sessoesDoFilme.add(s);
            }
        }
        return sessoesDoFilme;
    }

    // metodo para procurar sessoes no repositorio a partir do dia
    @Override
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