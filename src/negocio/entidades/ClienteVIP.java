package negocio.entidades;

import java.io.Serial;
import java.util.ArrayList;

public class ClienteVIP extends Cliente{
    @Serial
    private static final long serialVersionUID = -1293847192038472031L;
    public ClienteVIP(String nome, String nomeDeUsuario, String senha, ArrayList<Ingresso> ingressosComprados) {
        super(nome, nomeDeUsuario, senha, new ArrayList<>(ingressosComprados));
    }

    @Override
    public double calcularDesconto(double valorCompra) {
        //desconto de 35% do VIP
        return valorCompra * 0.65;
    }

    public String toString() {
        return "Cliente:" +
                " | ID: " + getId() +
                " | Nome: " + getNome() +
                " | Nome de Usuário: " + getNomeDeUsuario() +
                " | Senha: " + getSenha() +
                " | Tipo: " + "VIP" +
                " |";
    }
}
