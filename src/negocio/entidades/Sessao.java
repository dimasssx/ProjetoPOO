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
    private final String id;
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

    public Assento getAssento(int fileira, int poltrona) {
        if (fileira < 0 || fileira >= assentos.length || poltrona < 0 || poltrona >= assentos[0].length) {
            return null;
        }
        return assentos[fileira][poltrona];
    }

    public String getId() {
        return id;
    }
    
    public Sala getSala() {
        return this.sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public Filme getFilme() {
        return this.filme;
    }

    public void setFilme(Filme filme) {
        this.filme = filme;
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
    
    public void setAssentos(Assento[][] assentos) {
        this.assentos = assentos;
    }

    public double getValorIngresso() {
        return valorIngresso;
    }

    public void setValorIngresso(double valorIngresso) {
        this.valorIngresso = valorIngresso;
    }

    public void setDia(MonthDay dia) {
        this.dia = dia;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
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
