package negocio.entidades;

import java.io.Serial;
import java.io.Serializable;

public class Ingresso implements Serializable {

    @Serial
    private static final long serialVersionUID = -4009776605163947717L;
    private Sessao sessao;
    private Assento assento;

    public Ingresso(Sessao sessao, Assento assento) {
        this.sessao = sessao;
        this.assento = assento;
    }

    public Sessao getSessao() {
        return sessao;
    }

    public Assento getAssento() {
        return assento;
    }

    public void setSessao(Sessao sessao) {
        this.sessao = sessao;
    }

    public void setAssento(Assento assento) {
        this.assento = assento;
    }

    @Override
    public String toString() {
        return "Filme: " + sessao.getFilme().getTitulo() +
               " | Sala: " + sessao.getSala().getCodigo() + 
               " | Fileira: " + (char)('A' + (assento.getFileira()-1)) +
               " | Poltrona: " + assento.getPoltrona() +
               " | Data: " + sessao.getDiaFormatado() +
               " | Horário: " + sessao.getHorario();
    }
}
