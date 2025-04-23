package negocio;

import dados.*;
import negocio.entidades.*;
import negocio.exceptions.salas.CodigoSalaJaExisteException;
import negocio.exceptions.salas.LimiteDeSalasExcedidoException;
import negocio.exceptions.salas.NenhumaSalaEncontradaException;
import negocio.exceptions.salas.SalaNaoEncontradaException;

import java.util.ArrayList;

public class SalasNegocio {
    private final IRepositorioSalas repositorioSalas;
    private final IRepositorioSessoes repositorioSessoes;
    private final IRepositorioIDs repositorioIDs;

    public SalasNegocio(IRepositorioSalas repositorioSalas,IRepositorioSessoes repositorioSessoes){
        this.repositorioSalas = repositorioSalas;
        this.repositorioSessoes = repositorioSessoes;
        this.repositorioIDs = new RepositorioIDsArquivoBinario();
    }

    public void adicionarSala(String codigo,String tipo, int linhas, int colunas) throws CodigoSalaJaExisteException, LimiteDeSalasExcedidoException {
        Sala sala;

        if (tipo.equalsIgnoreCase("2D")){
            sala = new Sala2D(codigo,linhas, colunas);
        }else if (tipo.equalsIgnoreCase("3D")){
            sala = new Sala3D(codigo,linhas, colunas);
        }else{
            throw new IllegalArgumentException("Tipo de sala inválido. Use 2D ou 3D.");
        }

        if (repositorioSalas.listarSalas().stream().anyMatch(s -> s.getCodigo().equals(sala.getCodigo()))) {
            throw new CodigoSalaJaExisteException("O código " + sala.getCodigo() + " já existe.");
        }
        if (sala instanceof Sala2D && repositorioSalas.listarSalas().stream().filter(s -> s instanceof Sala2D).count() >= 2) {
                throw new LimiteDeSalasExcedidoException("O limite de 2 salas 2D já foi atingido.");
        }
        if (sala instanceof Sala3D && repositorioSalas.listarSalas().stream().filter(s -> s instanceof Sala3D).count() >= 1) {
            throw new LimiteDeSalasExcedidoException("O limite de 1 sala 3D já foi atingido.");
        }
        repositorioSalas.adicionarSala(sala);
    }
    public void removerSala(String id) throws SalaNaoEncontradaException {
        Sala salaDesejada = repositorioSalas.procurarSala(id);
        ArrayList<Sessao> sessoesremovidas;
        if (salaDesejada != null){
            sessoesremovidas = repositorioSessoes.procurarSessoesPorIdSala(id);
            //remove as sessoes vinculadas a ela
            if(sessoesremovidas!= null){
                for (Sessao s : sessoesremovidas){
                    repositorioSessoes.removerSessao(s);
                    repositorioIDs.removerID(GeradorID.getInstancia().getPrefixoSessao(), s.getId());
                }
            }
            repositorioSalas.removerSala(salaDesejada);
            repositorioIDs.removerID(GeradorID.getInstancia().getPrefixoSala(), salaDesejada.getId());
        }
        else throw new SalaNaoEncontradaException("Essa sala nao foi encontrada");
    }
    public Sala procurarSala(String codigo) throws SalaNaoEncontradaException {
        Sala saladesejada = repositorioSalas.procurarSala(codigo);
        if (saladesejada != null){
            return saladesejada;
        } else throw new SalaNaoEncontradaException("Sala nao foi encontrada");
    }
    public ArrayList<Sala> listarSalas() throws NenhumaSalaEncontradaException {
        ArrayList<Sala> salas = repositorioSalas.listarSalas();
        if (salas.isEmpty()){
            throw new NenhumaSalaEncontradaException();
        } else return repositorioSalas.listarSalas();
    }
 }

