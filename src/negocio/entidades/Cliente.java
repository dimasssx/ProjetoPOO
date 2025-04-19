package negocio.entidades;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import negocio.GeradorIDNegocio;

public abstract class Cliente implements Serializable {

    @Serial
    private static final long serialVersionUID = -4009776605163947716L;
    private String id;
    private String nome;
    private String login;
    private String senha;
    private ArrayList<Ingresso> ingressosComprados;

    public Cliente(String nome,String login, String senha){
        this.id = GeradorIDNegocio.getInstancia().gerarId(GeradorIDNegocio.getInstancia().getPrefixoCliente());
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.ingressosComprados = new ArrayList<>();
    }

    /*construtor alternativo passando ingressos comprados, pois na situação de tornar o cliente vip, é necessário utilizar
    esse construtor alternativo*/
    public Cliente(String nome,String login, String senha, ArrayList<Ingresso> ingressosComprados){
        this.id = GeradorIDNegocio.getInstancia().gerarId(GeradorIDNegocio.getInstancia().getPrefixoCliente());
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.ingressosComprados = ingressosComprados;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public ArrayList<Ingresso> getIngressosComprados(){
        return this.ingressosComprados;
    }

    public void adicionarIngresso(Ingresso ingresso){
        this.ingressosComprados.add(ingresso);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id) || Objects.equals(login, cliente.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }
}