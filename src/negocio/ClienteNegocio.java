package negocio;

import dados.IRepositorioClientes;
import negocio.entidades.*;
import negocio.exceptions.*;
import java.util.ArrayList;
import negocio.SessoesNegocio;

import java.util.ArrayList;

public class ClienteNegocio {

    public class ClienteNegocio {
        IRepositorioClientes clientes;
        SessoesNegocio sessao;
        Scanner scanner;

        public ClienteNegocio(IRepositorioClientes clientes) {
            this.clientes = clientes;
        }

        public void adicionarCliente(String nome, String login, String senha) throws ClienteJaExisteException {
            if (!clientes.existe(login)) {
                Cliente cliente = new ClientePadrao(nome, login, senha);
                clientes.adicionarCliente(cliente);
            } else {
                throw new ClienteJaExisteException();
            }
        }

        public void imprimirClientes() {
            clientes.imprimir();
        }

        public Cliente buscarCliente(String login, String senha) {
            return clientes.retornarCliente(login, senha);
        }

        public boolean validarCliente(String login, String senha) throws ClienteNaoEncontradoException {
            if (clientes.validarCliente(login, senha)) {
                return true;
            } else {
                throw new ClienteNaoEncontradoException();
            }
        }

        public void tornarVIP(Cliente cliente){
            Cliente seTornarVIP = new ClienteVIP(cliente.getNome(), cliente.getLogin(), cliente.getSenha(), cliente.getIngressosComprados());
            clientes.adicionarCliente(seTornarVIP);
            clientes.removerCliente(cliente);
        }

        public void cancelarVIP(Cliente cliente){
            Cliente voltarAoPadrao = new ClientePadrao(cliente.getNome(), cliente.getLogin(), cliente.getSenha(), cliente.getIngressosComprados());
            clientes.adicionarCliente(voltarAoPadrao);
            clientes.removerCliente(cliente);
        }

        public Ingresso comprarIngresso(Cliente cliente, Sessao sessao, Scanner input) throws AssentoIndisponivelException {
            sessao.mostrarAssentos();
            while (true) {
                try {
                    System.out.println("\nDigite o assento desejado ou 0 para cancelar");
                    String entrada = scanner.nextLine().trim().toUpperCase();

                    if (entrada.equals("0")) {
                        System.out.println("Operação cancelada");
                        return null;
                    }

                    int fileira = entrada.charAt(0) - 'A';
                    int poltrona = Integer.parseInt(entrada.substring(1)) - 1;

                    if (sessao.reservarAssento(fileira, poltrona)) {
                        Assento assento = sessao.getAssento(fileira, poltrona);
                        Ingresso ingresso = new Ingresso(sessao, assento);

                        System.out.println("Ingresso Adquirido para o assento "+ entrada);
                        ingresso.gerarIngresso();
                        return ingresso;
                    }else{
                        System.out.println("Assento" + entrada + "indisponível");
                    }


                }  catch (AssentoInvalidoException e) {
                    System.out.println("Número do assento inválido");
                } catch (AssentoIndisponivelException e) {
                    System.out.println("Erro ao fazer reserva: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Erro inesperado: " + e.getMessage());
                }
            }
        }
    }
}
