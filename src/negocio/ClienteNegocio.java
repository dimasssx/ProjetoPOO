package negocio;

import dados.IRepositorioClientes;
import negocio.entidades.*;
import negocio.exceptions.usuario.ClienteJaExisteException;
import negocio.exceptions.usuario.ClienteNaoEncontradoException;

public class ClienteNegocio {
    private IRepositorioClientes repositorioClientes;

    public ClienteNegocio(IRepositorioClientes repositorioClientes) {
        this.repositorioClientes = repositorioClientes;
    }

    //gerenciamento do cliente

    public void adicionarCliente(String nome, String nomeDeUsuario, String senha) throws ClienteJaExisteException {
        if (!repositorioClientes.existe(nomeDeUsuario)) {
            Cliente cliente = new ClientePadrao(nome, nomeDeUsuario, senha);
            repositorioClientes.adicionarCliente(cliente);
        } else {
            throw new ClienteJaExisteException();
        }
    }

    public Cliente buscarCliente(String nomeDeUsuario, String senha) throws ClienteNaoEncontradoException {
        Cliente cliente = repositorioClientes.retornarCliente(nomeDeUsuario, senha);
        if(cliente == null) throw new ClienteNaoEncontradoException();
        return cliente;
    }

    public boolean validarCliente(String nomeDeUsuario, String senha) throws ClienteNaoEncontradoException {
        if (repositorioClientes.validarCliente(nomeDeUsuario, senha)) {
            return true;
        } else {
            throw new ClienteNaoEncontradoException();
        }
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

    public void alterarSenha(Cliente cliente, String novaSenha) {
        cliente.setSenha(novaSenha);
        repositorioClientes.atualizarCliente(cliente);
    }

    //compra de ingressos

//    public Ingresso comprarIngresso(Cliente cliente, Sessao sessao, Scanner input) throws AssentoIndisponivelException {
//        sessao.mostrarAssentos();
//        while (true) {
//            try {
//                System.out.println("\nDigite o assento desejado ou 0 para cancelar");
//                String entrada = scanner.nextLine().trim().toUpperCase();
//
//                if (entrada.equals("0")) {
//                    System.out.println("Operação cancelada");
//                    return null;
//                }
//
//                int fileira = entrada.charAt(0) - 'A';
//                int poltrona = Integer.parseInt(entrada.substring(1)) - 1;
//
//                if (sessao.reservarAssento(fileira, poltrona)) {
//                    Assento assento = sessao.getAssento(fileira, poltrona);
//                    Ingresso ingresso = new Ingresso(sessao, assento);
//
//                    System.out.println("Ingresso Adquirido para o assento "+ entrada);
//                    ingresso.gerarIngresso();
//                    return ingresso;
//                }else{
//                    System.out.println("Assento" + entrada + "indisponível");
//                }
//
//
//            } catch (AssentoInvalidoException e) {
//                System.out.println("Número do assento inválido");
//            } catch (AssentoIndisponivelException e) {
//                System.out.println("Erro ao fazer reserva: " + e.getMessage());
//            } catch (Exception e) {
//                System.out.println("Erro inesperado: " + e.getMessage());
//            }
//        }
//    }
}

