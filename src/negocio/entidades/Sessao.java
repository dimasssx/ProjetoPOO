package negocio.entidades;

import java.io.Serial;
import java.io.Serializable;
import java.time.MonthDay;
import java.time.LocalTime;
import exceptions.AssentoIndisponivelException;
import negocio.entidades.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Sessao implements Serializable {
    @Serial
    private static final long serialVersionUID = -4009776605163947716L;
    private LocalTime horario;
    private MonthDay dia;
    private Filme filme;
    private Sala sala;
    private Assento[][] assentos;
    private double valorIngresso;

    public Sessao(Filme filme, String horario,Sala sala,String dia){
        this.filme = filme;
        this.sala = sala;
        this.horario = LocalTime.parse(horario);
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MM");
        this.dia = MonthDay.parse(dia, formater);
        this.assentos = new Assento[sala.getFileiras()][sala.getAssentosPorFileira()];
        inicializarAssentos();
    }

    private void inicializarAssentos() {
        for (int i = 0; i < sala.getFileiras(); i++) {
            for (int j = 0; j < sala.getAssentosPorFileira(); j++) {
                assentos[i][j] = new Assento(i + 1, j + 1);
            }
        }
    }

    public Assento getAssento(int fileira, int poltrona) {
        if (fileira < 0 || fileira >= assentos.length || poltrona < 0 || poltrona >= assentos[0].length) {
            return null;
        }
        return assentos[fileira][poltrona];
    }

    public Sala getSala() {
        return this.sala;
    }
    public Filme getFilme() {
        return this.filme;
    }
    public LocalTime getHorario() {
        return this.horario;
    }

    public MonthDay getDia() {
        return this.dia;
    }

    public String getDiaFormatado() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        return this.dia.format(formatter);
    }

    public double getValorIngresso() {
        return sala.calcularPrecoIngresso();
    }

    public void mostrarAssentos() {
        System.out.println("Mapa de assentos - " + filme.getTitulo() + " às " + horario + " (" + getDiaFormatado() + ")");
        for (int i = 0; i < assentos.length; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < assentos[i].length; j++) {
                if (assentos[i][j].getReservado()) {
                    System.out.print("[X] ");
                } else {
                    System.out.print("[ ] ");
                }
            }
            System.out.println();
        }
        System.out.print("   ");
        for (int j = 0; j < assentos[0].length; j++) {
            System.out.print((j + 1) + "   ");
        }
        System.out.println("\nLegenda: [ ] disponível | [X] reservado");
    }

    public boolean reservarAssento(int fileira, int numero) throws AssentoIndisponivelException {
        if (fileira >= 0 && fileira < assentos.length && numero >= 0 && numero < assentos[0].length) {
            if (!assentos[fileira][numero].getReservado()) {
                assentos[fileira][numero].reservar(); // Marca como reservado
                return true;
            }
        }
        return false; // Não conseguiu reservar
    }

    public int assentosDisponiveis(){
        int disponiveis = 0;
        for (int i = 0; i < assentos.length; i++) {
            for (int j = 0; j < assentos[i].length; j++) {
                if (!assentos[i][j].getReservado()) {
                    disponiveis++;
                }
            }
        }
        return disponiveis;
    }

    @Override
    public String toString() {
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MM");

        return filme + "| Horario: " + horario + " "+sala + getDiaFormatado();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Sessao sessao = (Sessao) obj;
        return horario.equals(sessao.horario) &&
                dia.equals(sessao.dia) &&
                sala.getCodigo().equals(sessao.sala.getCodigo());
    }

    @Override
    public int hashCode() {
        return horario.hashCode() + dia.hashCode() + sala.getCodigo().hashCode();
    }
}
