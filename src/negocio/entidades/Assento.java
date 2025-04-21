package negocio.entidades;

import java.io.Serial;
import java.io.Serializable;

import negocio.exceptions.assentos.AssentoIndisponivelException;

public class Assento implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final int fileira;
    private final int poltrona;
    private boolean isReservado;

    public Assento(int fileira, int poltrona) {
        this.fileira = fileira;
        this.poltrona = poltrona;
        this.isReservado = false;
    }

    public int getFileira() {
        return fileira;
    }

    public int getPoltrona() {
        return poltrona;
    }

    public boolean isReservado() {
        return isReservado;
    }

    public void reservar() throws AssentoIndisponivelException {
        if (isReservado) {
            throw new AssentoIndisponivelException();
        }
        isReservado = true;
    }

    public void cancelarReserva() {
        isReservado = false;
    }

    @Override
    public String toString() {
        return "" + (char) ('A' + (8 - fileira)) + poltrona + (isReservado ? "(R)" : "");
    }
}