package negocio.entidades;

import java.util.ArrayList;

public class ClienteVIP extends Cliente{

    public ClienteVIP(String nome, String login, String senha, ArrayList<Ingresso> ingressosComprados) {
        super(nome, login, senha, new ArrayList<>(ingressosComprados));
    }
}
