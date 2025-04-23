package negocio;

import dados.IRepositorioClientes;
import java.util.ArrayList;
import negocio.entidades.*;
import negocio.exceptions.usuario.ClienteNaoEncontradoException;
import negocio.exceptions.usuario.UsuarioJaExisteException;

public class ClientesNegocio {
    private final IRepositorioClientes repositorioClientes;
    public ClientesNegocio(IRepositorioClientes repositorioClientes) {
        this.repositorioClientes = repositorioClientes;
    }
    public void adicionarCliente(String nome, String nomeDeUsuario, String senha) throws UsuarioJaExisteException {
        if (!repositorioClientes.existe(nomeDeUsuario)) {
            Cliente cliente = new ClientePadrao(nome, nomeDeUsuario, senha);
            repositorioClientes.adicionarCliente(cliente);
        } else {
            throw new UsuarioJaExisteException();
        }
    }

    public Cliente buscarCliente(String nomeDeUsuario, String senha) throws ClienteNaoEncontradoException {
        Cliente cliente = repositorioClientes.retornarCliente(nomeDeUsuario, senha);
        if(cliente == null) throw new ClienteNaoEncontradoException();
        return cliente;
    }

    public void alterarSenha(Cliente cliente, String novaSenha) {
        cliente.setSenha(novaSenha);
        repositorioClientes.atualizarCliente(cliente);
    }

    public Cliente tornarVIP(Cliente cliente){
        Cliente seTornarVIP = new ClienteVIP(cliente.getNome(), cliente.getNomeDeUsuario(), cliente.getSenha(), cliente.getIngressosComprados());
        repositorioClientes.adicionarCliente(seTornarVIP);
        repositorioClientes.removerCliente(cliente);
        return seTornarVIP;
    }

    public Cliente cancelarVIP(Cliente cliente){
        Cliente voltarAoPadrao = new ClientePadrao(cliente.getNome(), cliente.getNomeDeUsuario(), cliente.getSenha(), cliente.getIngressosComprados());
        repositorioClientes.adicionarCliente(voltarAoPadrao);
        repositorioClientes.removerCliente(cliente);
        return voltarAoPadrao;
    }

    public double calcularValorTotalComTipos(Cliente cliente, Sessao sessao, ArrayList<Ingresso> ingressos,ArrayList<Lanche> lanches) {
        double valorTotal = 0;
        double valorUnitario = sessao.getValorIngresso();
        double valorLanches = 0;

        for (Lanche lanche: lanches){
            valorLanches += lanche.getPreco();
        }
        for (Ingresso ingresso : ingressos) {
            if (ingresso.getTipo().equals("Meia")) {
                valorTotal += valorUnitario * 0.5;
            } else {
                valorTotal += valorUnitario;
            }
        }
        valorTotal+= valorLanches;
        valorTotal = cliente.calcularDesconto(valorTotal);
        
        return valorTotal;
    }
    
    //adicionar ingressos comprados ao cliente
    public void adicionarIngressosAoCliente(Cliente cliente, ArrayList<Ingresso> ingressos) {
        for (Ingresso ingresso : ingressos) {
            cliente.adicionarIngressoComprado(ingresso);
        }
        repositorioClientes.atualizarCliente(cliente);
    }
}

