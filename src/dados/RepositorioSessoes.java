package dados;

import negocio.entidades.Sessao;
import java.io.*;
import java.time.LocalTime;
import java.time.MonthDay;
import java.util.ArrayList;


public class RepositorioSessoes implements IRepositorioSessoes, Serializable {
    @Serial
    private static final long serialVersionUID = 5527816120194523955L;
    ArrayList<Sessao> sessoes;
    private File file;


    public RepositorioSessoes() {
        file = new File("MovieTime/arquivos/sessoes.dat");
        if (file.exists()) {
            lerSessoes();
        } else {
            sessoes = new ArrayList<>();
            escreverSessoes();
        }
    }

    public void lerSessoes() {
        try (FileInputStream fis = new FileInputStream(file)) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            sessoes = (ArrayList<Sessao>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
            sessoes = new ArrayList<>();
        }
    }

    public void escreverSessoes() {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            ObjectOutput oos = new ObjectOutputStream(fos);
            oos.writeObject(sessoes);
        } catch (IOException e) {
            System.err.println(e.getMessage());
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

    // metodo para remover uma sessao do repositorio a partir dos atributos da sessao
    @Override
    public void removerSessao(LocalTime horario, String codigoSala, MonthDay dia) {
        for (Sessao s : sessoes) {
            if (s.getHorario().equals(horario) && s.getSala().getCodigo().equals(codigoSala) && s.getDia().equals(dia)) {
                sessoes.remove(s);
            }
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
    public void imprimir() {
        for (Sessao sessao : sessoes) {
            System.out.println(sessao);
        }

    }

    // metodo para procurar uma sessao no repositorio a partir de um objeto de sessao recebido
    @Override
    public Sessao procurarSessao(Sessao sessao) {
        Sessao sessaodesejada = null;
        for (Sessao s : sessoes) {
            if (s.equals(sessao)) {
                sessaodesejada = s;
            }
        }
        return sessaodesejada;

    }

    // metodo para procurar uma sessao no repositorio a partir dos atributos da sessao
    @Override
    public Sessao procurarSessao(MonthDay dia, String filme, LocalTime horario) {
        Sessao sessaodesejada = null;
        for (Sessao s : sessoes) {
            if (s.getHorario().equals(horario) && s.getFilme().getTitulo().equals(filme) && s.getDia().equals(dia)) {
                sessaodesejada = s;
                break;
            }
        }
        return sessaodesejada;
    }

    // metodo para procurar sessoes no repositorio a partir do nome do filme
    public ArrayList<Sessao> procurarSessao(String filme) {
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
        return sessoes.contains(sessao);
    }

    // metodo para retornar todas as sessoes do repositorio
    @Override
    public ArrayList<Sessao> retornarTodas() {
        ArrayList<Sessao> sessoesFilmes = new ArrayList<>();
        for (Sessao s : sessoes) {
            if (s != null) {
                sessoesFilmes.add(s);
            }
        }
        return sessoesFilmes;
    }
}