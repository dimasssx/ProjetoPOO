package negocio.entidades;

import java.io.Serial;
import java.io.Serializable;

public class Filme implements Serializable {

    @Serial
    private static final long serialVersionUID = -4009776605163947716L;
    private String titulo;
    private String genero;
    private String duracao;
    private String classificacao;

    public Filme(String titulo,String genero,String duracao,String classificacao){
        this.titulo = titulo;
        this.genero = genero;
        this.duracao = duracao;
        this.classificacao = classificacao;
    }

    public String getDuracao() {
        return duracao;
    }
    public String getGenero() {
        return genero;
    }
    public String getClassificacao() {
        return classificacao;
    }
    public String getTitulo(){
        return titulo;
    }
    public void setDuracao(String duracao){
        this.duracao = duracao;
    }
    public void setGenero(String genero){
        this.genero = genero;
    }
    public void setClassificacao(String classificacao){
        this.classificacao = classificacao;
    }
    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    @Override
    public String toString() {
        return "Titulo: " + titulo +
                ", Genero: " + genero +
                ", Duração: " + duracao +
                ", Classificação: " + classificacao;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Filme filme = (Filme) obj;
        return titulo.equals(filme.getTitulo());
    }

}

