package negocio.entidades;

import java.io.Serial;
import java.io.Serializable;

public class Sala3D extends Sala implements Serializable {
    @Serial
    private static final long serialVersionUID = -4009776605163947716L;

    public Sala3D(String codigo, int fileiras, int assentosPorFileira) {
        super(codigo, fileiras, assentosPorFileira);
    }

    @Override
    public double calcularPrecoIngresso() {
        return precoBaseIngresso * 1.8;
    }

    @Override
    public String toString() {
        return "Sala: " + getCodigo() + ", Tipo: 3D"+
                ", Fileiras: " + getFileiras() +
                ", Assentos por Fileiras:" + getAssentosPorFileira();
    }
    public String getTipo(){
        return "3D";
    }
}