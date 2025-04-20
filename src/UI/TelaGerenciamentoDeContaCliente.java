package UI;

import fachada.Movietime;
import java.util.Scanner;
import negocio.entidades.Cliente;
import negocio.entidades.ClientePadrao;
import negocio.entidades.ClienteVIP;
import negocio.entidades.Ingresso;

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
                System.out.println("3 - Tornar-se VIP (você irá precisar de um cartão de crédito)");
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
                        String numeroCartaoCredito = lerDado("Numero do Cartão");
                        String titularCartaoCredito = lerDado("Titular do Cartão");
                        double valorVIP = 57.60;
                        System.out.println(fachada.getFachadaCliente().pagarComCredito(numeroCartaoCredito,titularCartaoCredito,valorVIP));
                        tornarVIP();
                    } else if (cliente instanceof ClienteVIP) {
                        cancelarVIP();
                    }
                    return;
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

        fachada.getFachadaCliente().alterarSenha(cliente, novaSenha);
        System.out.println("Senha atualizada com sucesso!");
    }

    private void listarIngressos() {
        if (cliente.getIngressosComprados().isEmpty()) {
            System.out.println("Você ainda não comprou nenhum ingresso.");
            return;
        }
        System.out.println("\n=== Ingressos comprados ===");
        int contador = 1;
        for (Ingresso ingresso : cliente.getIngressosComprados()) {
            System.out.println(contador + ". " + ingresso.toString());
            contador++;
        }
    }

    private void tornarVIP() {
        fachada.getFachadaCliente().getClienteNegocio().tornarVIP(cliente);
        System.out.println("\033[92m Parabéns! Agora você é um cliente VIP e tem 35% de desconto na compra de ingressos! \033[0m");
        System.out.println("AVISO: Seu status como Cliente VIP será atualizado ao realizar novamente seu login!");
    }

    private void cancelarVIP() {
        fachada.getFachadaCliente().getClienteNegocio().cancelarVIP(cliente);
        System.out.println("\033[92m Você voltou a ser um Cliente Padrão. \033[0m");
        System.out.println("AVISO: Seu status como Cliente Padrão será atualizado ao realizar novamente seu login!");
    }

    private String lerDado(String campo) {
        System.out.print(campo + ": ");
        while(true){
            String dado = scanner.nextLine().trim();

            if (dado.equals("0")) {
                System.out.println("Operação cancelada.");
                return null;
            }
            if (dado.isEmpty()) {
                System.err.println(campo + " não pode estar vazio!");
                continue;
            }
            return dado;
        }
    }
}