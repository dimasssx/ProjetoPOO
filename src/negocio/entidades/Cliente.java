package negocio.entidades;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import negocio.GeradorIDNegocio;

public abstract class Cliente implements Serializable {

    @Serial
    private static final long serialVersionUID = -4009776605163947716L;
    private final String id;
    private String nome;
    private final String nomeDeUsuario;
    private String senha;
    private ArrayList<Ingresso> ingressosComprados;

    public Cliente(String nome, String nomeDeUsuario, String senha){
        this.id = GeradorIDNegocio.getInstancia().gerarId(GeradorIDNegocio.getInstancia().getPrefixoCliente());
        this.nome = nome;
        this.nomeDeUsuario = nomeDeUsuario;
        this.senha = senha;
        this.ingressosComprados = new ArrayList<>();
    }

    /*construtor alternativo passando ingressos comprados, pois na situação de tornar o cliente vip, é necessário utilizar
    esse construtor alternativo*/
    public Cliente(String nome, String nomeDeUsuario, String senha, ArrayList<Ingresso> ingressosComprados){
        this.id = GeradorIDNegocio.getInstancia().gerarId(GeradorIDNegocio.getInstancia().getPrefixoCliente());
        this.nome = nome;
        this.nomeDeUsuario = nomeDeUsuario;
        this.senha = senha;
        this.ingressosComprados = ingressosComprados;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getNomeDeUsuario() {
        return nomeDeUsuario;
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

    public void adicionarIngressoComprado(Ingresso ingresso) {
        this.ingressosComprados.add(ingresso);
    }

    public abstract double calcularDesconto(double valorCompra);

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id) || Objects.equals(nomeDeUsuario, cliente.nomeDeUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomeDeUsuario);
    }
}