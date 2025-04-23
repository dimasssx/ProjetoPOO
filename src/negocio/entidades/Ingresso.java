package negocio.entidades;

import negocio.GeradorIDNegocio;

import java.io.Serial;
import java.io.Serializable;

public class Ingresso implements Serializable {

    @Serial
    private static final long serialVersionUID = -4009776605163947717L;
    private String id;
    private Sessao sessao;
    private Assento assento;
    private String tipo; // meia ou inteira

    public Ingresso(Sessao sessao, Assento assento, String tipo) {
        this.id = GeradorIDNegocio.getInstancia().gerarId(GeradorIDNegocio.getInstancia().getPrefixoIngresso());
        this.sessao = sessao;
        this.assento = assento;
        this.tipo = tipo;
    }
    public String getId(){
        return id;
    }
    public Sessao getSessao() {
        return sessao;
    }
    public Assento getAssento() {
        return assento;
    }
    public String getTipo() {
        return tipo;
    }
    public void setSessao(Sessao sessao) {
        this.sessao = sessao;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Filme: " + sessao.getFilme().getTitulo() +
               " | Sala: " + sessao.getSala().getCodigo() + 
               " | Fileira: " + (char)('A' + (assento.getFileira()-1)) +
               " | Poltrona: " + assento.getPoltrona() +
               " | Data: " + sessao.getDiaFormatado() +
               " | Horário: " + sessao.getHorario() +
               " | Tipo: " + tipo;
    }
}
