package UI;

import fachada.Movietime;
import negocio.entidades.Cliente;
import negocio.entidades.ClientePadrao;
import negocio.entidades.ClienteVIP;
import negocio.entidades.Ingresso;

import java.util.Scanner;

public class TelaGerenciamentoDeContaCliente {

    private final Movietime fachada;
    private final Cliente cliente;
    private final Scanner scanner;

    public TelaGerenciamentoDeContaCliente(Movietime fachada, Cliente cliente) {
        this.fachada = fachada;
        this.cliente = cliente;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        while (true) {
            System.out.println("\nGerenciamento de Conta - " + cliente.getNome());
            System.out.println("1 - Alterar Senha");
            System.out.println("2 - Ver meus ingressos comprados");

            if (cliente instanceof ClientePadrao) {
                System.out.println("3 - Tornar-se VIP");
                System.out.println("4 - Voltar");
            } else if (cliente instanceof ClienteVIP) {
                System.out.println("3 - Cancelar VIP");
                System.out.println("4 - Voltar");
            }

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    alterarSenha();
                    break;
                case "2":
                    listarIngressos();
                    break;
                case "3":
                    if (cliente instanceof ClientePadrao) {
                        tornarVIP();
                    } else if (cliente instanceof ClienteVIP) {
                        cancelarVIP();
                    }
                    break;
                case "4":
                    return;
                default:
                    System.err.println("Opção inválida.");
            }
        }
    }

    private void alterarSenha() {
        System.out.println("Digite a nova senha:");
        String novaSenha = scanner.nextLine();
        if (novaSenha.equals("0")) {
            System.out.println("Operação cancelada.");
            return;
        }
        if (novaSenha.isEmpty()) {
            System.err.println("A senha não pode estar vazia.");
            return;
        }
        cliente.setSenha(novaSenha);
        System.out.println("Senha atualizada com sucesso!");
    }

    private void listarIngressos() {
        if (cliente.getIngressosComprados().isEmpty()) {
            System.out.println("Você ainda não comprou nenhum ingresso.");
            return;
        }
        System.out.println("Ingressos comprados:");
        for (Ingresso ingresso : cliente.getIngressosComprados()) {
            ingresso.gerarIngresso();
        }
    }

    private void tornarVIP() {
        fachada.getFachadaCliente().getClienteNegocio().tornarVIP(cliente);
        System.out.println("Parabéns! Agora você é um cliente VIP e tem 35% de desconto na compra de ingressos!");
    }

    private void cancelarVIP() {
        fachada.getFachadaCliente().getClienteNegocio().cancelarVIP(cliente);
        System.out.println("Você voltou a ser um cliente padrão.");
    }
}