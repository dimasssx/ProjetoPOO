package negocio.entidades;

import java.io.Serial;
import java.util.ArrayList;

public class ClientePadrao extends Cliente{
    @Serial
    private static final long serialVersionUID = 5873192035879321047L;
    public ClientePadrao(String nome, String login, String senha) {
        super(nome, login, senha);
    }

    public ClientePadrao(String nome, String login, String senha, ArrayList<Ingresso> ingressosComprados){
        super(nome, login, senha, new ArrayList<>(ingressosComprados));
    }
    @Override
    public String toString() {
        return "Cliente:" +
                " | ID: " + getId() +
                " | Nome: " + getNome() +
                " | Login: " + getLogin() +
                " | Senha: " + getSenha()  +
                " | Tipo: " + "Padrao" +
                " |";
    }
}
