package negocio.entidades;

public class Ingresso {

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

    public String toString() {
        return "Filme: " + sessao.getFilme() +
                "| Fileira:" + assento.getFileira() +
                "| Poltrona da Fileira:" + assento.getPoltrona() +
                "| Dia:" + sessao.getDia() +
                "| Horário:" + sessao.getHorario();
    }
}
