package negocio;

import dados.*;
import negocio.exceptions.CodigoSalaJaExisteException;
import negocio.exceptions.LimiteDeSalasExcedidoException;
import negocio.exceptions.NenhumaSalaEncontradaException;
import negocio.exceptions.SalaNaoEncontradaException;
import negocio.entidades.Sala;
import negocio.entidades.Sala2D;
import negocio.entidades.Sala3D;

import java.util.ArrayList;

public class SalasNegocio {
    IRepositorioSalas salas;

    public SalasNegocio(IRepositorioSalas salas){
        this.salas = salas;
    }

    public void adicionarSala(Sala sala) throws CodigoSalaJaExisteException, LimiteDeSalasExcedidoException {

        if (salas.listarSalas().stream().anyMatch(s -> s.getCodigo().equals(sala.getCodigo()))) {
            throw new CodigoSalaJaExisteException("O código " + sala.getCodigo() + " já existe.");
        }

        if (sala instanceof Sala2D && salas.listarSalas().stream().filter(s -> s instanceof Sala2D).count() >= 2) {
            throw new LimiteDeSalasExcedidoException("O limite de 2 salas 2D já foi atingido.");
        }

        if (sala instanceof Sala3D && salas.listarSalas().stream().filter(s -> s instanceof Sala3D).count() >= 1) {
            throw new LimiteDeSalasExcedidoException("O limite de 1 sala 3D já foi atingido.");
        }

        salas.adicionarSala(sala);
    }

    public void removerSala(String codigo) throws SalaNaoEncontradaException {
        Sala salaDesejada = salas.procurarSala(codigo);
        if (salaDesejada != null){
            salas.removerSala(salaDesejada);
        }else throw new SalaNaoEncontradaException("Essa sala nao foi encontrada");

    }

    public Sala procurarSala(String codigo) throws SalaNaoEncontradaException {
        Sala saladesejada = salas.procurarSala(codigo);
        if (saladesejada != null){
            return saladesejada;
        }else throw new SalaNaoEncontradaException("Sala nao foi encontrada");
    }
    public void atualizarSala(Sala sala) throws SalaNaoEncontradaException{
        Sala s = salas.procurarSala(sala.getCodigo());
        if (s != null){
            salas.atualizarSala(s);
        }else {
            throw new SalaNaoEncontradaException("Nenhuma sala encontrada");
        }
    }
    public ArrayList<Sala> listarSalas() throws NenhumaSalaEncontradaException {
        if (salas.listarSalas().isEmpty()){
            throw new NenhumaSalaEncontradaException();
        }else return salas.listarSalas();
    }

}
