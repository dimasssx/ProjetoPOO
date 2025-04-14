package negocio.entidades;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cliente implements Serializable {

    @Serial
    private static final long serialVersionUID = -4009776605163947716L;
    private String nome;
    private String login;
    private String senha;
    private List<Ingresso> ingressosComprados = new ArrayList<>();

    public Cliente(String nome,String login, String senha){
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Ingresso> getIngressosComprados(){
        return this.ingressosComprados;
    }

    public void adicionarIngresso(Ingresso ingresso){
        this.ingressosComprados.add(ingresso);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + nome + '\'' +
                ", login='" + login + '\'' +
                ", senha='" + senha + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(login, cliente.login);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(login);
    }
}