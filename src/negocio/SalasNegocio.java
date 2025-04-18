package negocio;

import dados.*;
import negocio.entidades.Sessao;
import negocio.exceptions.CodigoSalaJaExisteException;
import negocio.exceptions.LimiteDeSalasExcedidoException;
import negocio.exceptions.NenhumaSalaEncontradaException;
import negocio.exceptions.SalaNaoEncontradaException;
import negocio.entidades.Sala;
import negocio.entidades.Sala2D;
import negocio.entidades.Sala3D;

import java.util.ArrayList;

public class SalasNegocio {
    IRepositorioSalas repositorioSalas;
    IRepositorioSessoes repositorioSessoes;

    public SalasNegocio(IRepositorioSalas repositorioSalas,IRepositorioSessoes repositorioSessoes){
        this.repositorioSalas = repositorioSalas;
        this.repositorioSessoes = repositorioSessoes;
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

    public void removerSala(String codigo) throws SalaNaoEncontradaException {
        Sala salaDesejada = repositorioSalas.procurarSala(codigo);
        ArrayList<Sessao> sessoesremovidas;
        if (salaDesejada != null){
            sessoesremovidas = repositorioSessoes.procurarSessaoporSala(codigo);
            if(sessoesremovidas!= null){
                for (Sessao s :sessoesremovidas){
                    repositorioSessoes.removerSessao(s);
                }
            }repositorioSalas.removerSala(salaDesejada);
        }else throw new SalaNaoEncontradaException("Essa sala nao foi encontrada");

    }

    public Sala procurarSala(String codigo) throws SalaNaoEncontradaException {
        Sala saladesejada = repositorioSalas.procurarSala(codigo);
        if (saladesejada != null){
            return saladesejada;
        }else throw new SalaNaoEncontradaException("Sala nao foi encontrada");
    }
//    public void atualizarSala(String codigo,String tipo, int linhas, int colunas) throws SalaNaoEncontradaException{
//
//        Sala s = null;
//
//        if (tipo.equalsIgnoreCase("2D")){
//            s = new Sala2D(codigo,linhas, colunas);
//            s = salas.procurarSala(s.getCodigo());
//        }else if (tipo.equalsIgnoreCase("3D")){
//            s = new Sala3D(codigo,linhas, colunas);
//            s = salas.procurarSala(s.getCodigo());
//        }
//
//        if (s != null){
//            salas.atualizarSala(s);
//        }else {
//            throw new SalaNaoEncontradaException("Nenhuma sala encontrada");
//        }
//    }
    public ArrayList<Sala> listarSalas() throws NenhumaSalaEncontradaException {
        if (repositorioSalas.listarSalas().isEmpty()){
            throw new NenhumaSalaEncontradaException();
        }else return repositorioSalas.listarSalas();
    }

 }

