package negocio.entidades;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import negocio.GeradorIDNegocio;

public class Sessao implements Serializable {

    @Serial
    private static final long serialVersionUID = -4009776605163947716L;
    private String id;
    private LocalTime horario;
    private MonthDay dia;
    private Filme filme;
    private Sala sala;
    private Assento[][] assentos;
    private double valorIngresso;

    public Sessao(Filme filme, String horario, Sala sala, String dia, double valorIngresso) {
        this.id = GeradorIDNegocio.getInstancia().gerarId(GeradorIDNegocio.getInstancia().getPrefixoSessao());
        this.filme = filme;
        this.valorIngresso = valorIngresso;
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

    public String getId() {
        return id;
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
        return "Sessao: " +
                "| ID: " + id +
                " | Filme: " + filme.getTitulo() +
                " | Sala: " + sala.getCodigo() + " (" + sala.getTipo() + ")" +
                " | Horário: " + horario +
                " | Data: " + getDiaFormatado() +
                " | Valor do Ingresso: " + valorIngresso +
                " |";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Sessao sessao = (Sessao) obj;
        return id.equals(sessao.id) || 
               (horario.equals(sessao.horario) &&
                dia.equals(sessao.dia) &&
                sala.getCodigo().equals(sessao.sala.getCodigo()));
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
