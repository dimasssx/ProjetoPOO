package negocio.entidades;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import negocio.GeradorIDNegocio;

public abstract class Sala implements Serializable {

    @Serial
    private static final long serialVersionUID = -4009776605163947716L;
    private final String id;
    private final String codigo;
    private final int fileiras;
    private final int assentosPorFileira;
    protected double precoBaseIngresso;

    public Sala(String codigo, int fileiras, int assentosPorFileira) {
        this.id = GeradorIDNegocio.getInstancia().gerarId(GeradorIDNegocio.getInstancia().getPrefixoSala());
        this.codigo = codigo;
        this.fileiras = fileiras;
        this.assentosPorFileira = assentosPorFileira;
    }
    
    public Sala(String codigo) {
        this.id = GeradorIDNegocio.getInstancia().gerarId(GeradorIDNegocio.getInstancia().getPrefixoSala());
        this.codigo = codigo;
        this.fileiras = 0;
        this.assentosPorFileira = 0;
    }
    
    public String getId() {
        return id;
    }
    
    public String getCodigo() {
        return codigo;
    }

    public int getFileiras() {
        return fileiras;
    }

    public int getAssentosPorFileira() {
        return assentosPorFileira;
    }

    public abstract double calcularPrecoIngresso();

    public abstract String getTipo();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sala sala = (Sala) o;
        return Objects.equals(id, sala.id) || Objects.equals(codigo, sala.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigo);
    }
}