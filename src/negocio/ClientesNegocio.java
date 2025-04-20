package negocio;

import dados.IRepositorioClientes;
import java.util.ArrayList;
import negocio.entidades.*;
import negocio.exceptions.usuario.ClienteNaoEncontradoException;
import negocio.exceptions.usuario.UsuarioJaExisteException;

public class ClientesNegocio {
    private IRepositorioClientes repositorioClientes;

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


    //calcular o valor total dos ingressos com desconto para VIP se aplicável
    public double calcularValorTotal(Cliente cliente, Sessao sessao, int quantidadeIngressos) {
        double valorUnitario = sessao.getValorIngresso();
        double valorTotal = valorUnitario * quantidadeIngressos;
        
        // Se o cliente for VIP, aplica desconto de 35%
        if (cliente instanceof ClienteVIP) {
            valorTotal = valorTotal * 0.65;
        }
        
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

