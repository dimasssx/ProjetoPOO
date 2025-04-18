package negocio;

import dados.IRepositorioClientes;
import negocio.entidades.*;
import negocio.exceptions.*;

public class ClienteNegocio {
    private IRepositorioClientes repositorio;

    public ClienteNegocio(IRepositorioClientes repositorio) {
        this.repositorio = repositorio;
    }

    //gerenciamento do cliente

    public void adicionarCliente(String nome, String login, String senha) throws ClienteJaExisteException {
        if (!repositorio.existe(login)) {
            Cliente cliente = new ClientePadrao(nome, login, senha);
            repositorio.adicionarCliente(cliente);
        } else {
            throw new ClienteJaExisteException();
        }
    }

    public void imprimirClientes() {
        repositorio.imprimir();
    }

    public Cliente buscarCliente(String login, String senha) {
        return repositorio.retornarCliente(login, senha);
    }

    public boolean validarCliente(String login, String senha) throws ClienteNaoEncontradoException {
        if (repositorio.validarCliente(login, senha)) {
            return true;
        } else {
            throw new ClienteNaoEncontradoException();
        }
    }

    public void tornarVIP(Cliente cliente){
        Cliente seTornarVIP = new ClienteVIP(cliente.getNome(), cliente.getLogin(), cliente.getSenha(), cliente.getIngressosComprados());
        repositorio.adicionarCliente(seTornarVIP);
        repositorio.removerCliente(cliente);
    }

    public void cancelarVIP(Cliente cliente){
        Cliente voltarAoPadrao = new ClientePadrao(cliente.getNome(), cliente.getLogin(), cliente.getSenha(), cliente.getIngressosComprados());
        repositorio.adicionarCliente(voltarAoPadrao);
        repositorio.removerCliente(cliente);
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

