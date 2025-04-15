package negocio.entidades;

import java.util.ArrayList;

public class ClientePadrao extends Cliente{

    public ClientePadrao(String nome, String login, String senha) {
        super(nome, login, senha);
    }

    public ClientePadrao(String nome, String login, String senha, ArrayList<Ingresso> ingressosComprados){
        super(nome, login, senha, new ArrayList<>(ingressosComprados));
    }
}
