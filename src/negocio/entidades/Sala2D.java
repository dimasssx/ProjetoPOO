package negocio.entidades;

import java.io.Serial;
import java.io.Serializable;

public class Sala2D extends Sala implements Serializable {

    @Serial
    private static final long serialVersionUID = -4009776605163947716L;
    public Sala2D(String codigo, int fileiras, int assentosPorFileira) {
        super(codigo, fileiras, assentosPorFileira);
    }

    @Override
    public double calcularPrecoIngresso() {
        return precoBaseIngresso;
    }

    public String getTipo(){
        return "2D";
    }

    @Override
    public String toString() {
        return
                "Sala: " + getCodigo() + ", Tipo: 2D"+
                        ", Fileiras: " + getFileiras() +
                        ", Assentos por fileira:" + getAssentosPorFileira();
    }
}