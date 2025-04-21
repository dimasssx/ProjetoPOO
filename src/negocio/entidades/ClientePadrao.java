package negocio.entidades;

import java.io.Serial;
import java.util.ArrayList;

public class ClientePadrao extends Cliente{
    @Serial
    private static final long serialVersionUID = 5873192035879321047L;

    public ClientePadrao(String nome, String nomeDeUsuario, String senha) {
        super(nome, nomeDeUsuario, senha);
    }

    public ClientePadrao(String nome, String nomeDeUsuario, String senha, ArrayList<Ingresso> ingressosComprados){
        super(nome, nomeDeUsuario, senha, new ArrayList<>(ingressosComprados));
    }

    @Override
    public double calcularDesconto(double valorCompra) {
        return valorCompra;
    }

    @Override
    public String toString() {
        return "Cliente:" +
                " | ID: " + getId() +
                " | Nome: " + getNome() +
                " | Nome de Usuário: " + getNomeDeUsuario() +
                " | Senha: " + getSenha()  +
                " | Tipo: " + "Padrao" +
                " |";
    }
}
