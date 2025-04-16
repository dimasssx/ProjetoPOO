package negocio.entidades;

import java.io.Serial;
import java.io.Serializable;
import java.time.MonthDay;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

    public Assento[][] getAssentos() {
        return assentos;
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
