package negocio.entidades;

import java.io.Serial;
import java.io.Serializable;

public class
Sala3D extends Sala implements Serializable {

    @Serial
    private static final long serialVersionUID = -4009776605163947716L;

    public Sala3D(String codigo, int fileiras, int assentosPorFileira) {
        super(codigo, fileiras, assentosPorFileira);
    }

    public String getTipo(){
        return "3D";
    }

    @Override
    public String toString() {
        return "Sala: " + getCodigo() + " | ID: " + getId() +
                " | Tipo: 3D" +
                " | Fileiras: " + getFileiras() +
                " | Assentos por Fileiras:" + getAssentosPorFileira() +
                " |";

    }
}