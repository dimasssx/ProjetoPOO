package UI;

import fachada.Movietime;
import java.util.Scanner;
import negocio.entidades.Cliente;
import negocio.entidades.ClientePadrao;
import negocio.entidades.ClienteVIP;
import negocio.entidades.Ingresso;
import static UI.Utils.ValidacaoEntradas.*;
public class TelaGerenciamentoDeContaCliente {

    private final Movietime fachada;
    private final Cliente cliente;
    private final Scanner scanner;

    // cores ANSI para melhorar a interface, vao estar presentes nos prints da tela para mudar a cor
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BOLD = "\u001B[1m";

    public TelaGerenciamentoDeContaCliente(Movietime fachada, Cliente cliente) {
        this.fachada = fachada;
        this.cliente = cliente;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        while (true) {
            imprimirCabecalho();
            imprimirInfoCliente();
            
            System.out.println("\n" + ANSI_BOLD + "═════════════════════════════════════════" + ANSI_RESET);
            System.out.println(ANSI_BOLD + "  MENU DE OPÇÕES" + ANSI_RESET);
            System.out.println(ANSI_BOLD + "═════════════════════════════════════════" + ANSI_RESET);
            
            System.out.println("1 - Alterar Senha");
            System.out.println("2 - Ver meus ingressos comprados");

            if (cliente instanceof ClientePadrao) {
                System.out.println("3 - " + "Tornar-se VIP" + " (você irá precisar de um cartão de crédito)");
                System.out.println("4 - Voltar");
            } else if (cliente instanceof ClienteVIP) {
                System.out.println("3 - " + "Cancelar VIP");
                System.out.println("4 - Voltar");
            }
            
            System.out.print("► ");
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
                        String numeroCartaoCredito;
                        while (true) {
                            numeroCartaoCredito = lerDado("Numero do Cartão (apenas números)");
                            if (numeroCartaoCredito == null) {
                                break;
                            }
                            
                            // Verificar se o cartão tem exatamente 16 dígitos e apenas números
                            if (!numeroCartaoCredito.matches("\\d{16}")) {
                                System.out.println(ANSI_RED + "Número de cartão inválido. O cartão deve ter exatamente 16 dígitos numéricos." + ANSI_RESET);
                                continue;
                            }
                            break;
                        }
                        
                        if (numeroCartaoCredito == null) {
                            continue;
                        }
                        
                        String titularCartaoCredito = lerDado("Titular do Cartão");
                        if (titularCartaoCredito == null) continue;
                        
                        double valorVIP = 57.80;
                        System.out.println("\n" + ANSI_BOLD + "═════════════════════════════════════════" + ANSI_RESET);
                        System.out.println(ANSI_BOLD + "  PROCESSANDO PAGAMENTO VIP" + ANSI_RESET);
                        System.out.println(ANSI_BOLD + "═════════════════════════════════════════" + ANSI_RESET);
                        System.out.println(fachada.getFachadaCliente().pagarComCredito(numeroCartaoCredito, titularCartaoCredito, valorVIP));
                        tornarVIP();
                    } else if (cliente instanceof ClienteVIP) {
                        cancelarVIP();
                    }
                    return;
                case "4":
                    System.out.println(ANSI_YELLOW + "Voltando para o menu principal..." + ANSI_RESET);
                    return;
                default:
                    System.out.println(ANSI_RED + "Opção inválida. Por favor, tente novamente." + ANSI_RESET);
            }
        }
    }
    
    private void imprimirCabecalho() {
        System.out.println("\n" + ANSI_BOLD);
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║              GERENCIAMENTO DE CONTA               ║");
        System.out.println("╚═══════════════════════════════════════════════════╝" + ANSI_RESET);
    }
    
    private void imprimirInfoCliente() {
        System.out.println("\n" + ANSI_BOLD + "┌─────────────────────────────────────────────┐" + ANSI_RESET);
        System.out.println(ANSI_BOLD + "│ Cliente: " + cliente.getNome() + ANSI_RESET);
        System.out.println(ANSI_BOLD + "│ Usuário: " + cliente.getNomeDeUsuario() + ANSI_RESET);
        System.out.println(ANSI_BOLD + "│ Tipo de conta: " + (cliente instanceof ClienteVIP ? "VIP 👑" : "Padrão") + ANSI_RESET);
        System.out.println(ANSI_BOLD + "└─────────────────────────────────────────────┘" + ANSI_RESET);
    }

    private void alterarSenha() {
        System.out.println("\n" + ANSI_BOLD + "═════════════════════════════════════════" + ANSI_RESET);
        System.out.println(ANSI_BOLD + "  ALTERAÇÃO DE SENHA" + ANSI_RESET);
        System.out.println(ANSI_BOLD + "═════════════════════════════════════════" + ANSI_RESET);
        System.out.println("Digite a nova senha (ou 0 para cancelar):");
        System.out.print("► ");
        
        String novaSenha = scanner.nextLine();
        if (novaSenha.equals("0")) {
            System.out.println(ANSI_YELLOW + "Operação cancelada." + ANSI_RESET);
            return;
        }
        if (novaSenha.isEmpty()) {
            System.out.println(ANSI_RED + "A senha não pode estar vazia." + ANSI_RESET);
            return;
        }

        fachada.getFachadaCliente().alterarSenha(cliente, novaSenha);
        System.out.println(ANSI_GREEN + "✓ Senha atualizada com sucesso!" + ANSI_RESET);
    }

    private void listarIngressos() {
        System.out.println("\n" + ANSI_BOLD + "═════════════════════════════════════════" + ANSI_RESET);
        System.out.println(ANSI_BOLD + "  MEUS INGRESSOS" + ANSI_RESET);
        System.out.println(ANSI_BOLD + "═════════════════════════════════════════" + ANSI_RESET);
        
        if (cliente.getIngressosComprados().isEmpty()) {
            System.out.println(ANSI_YELLOW + "Você ainda não comprou nenhum ingresso." + ANSI_RESET);
            return;
        }
        
        int contador = 1;
        for (Ingresso ingresso : cliente.getIngressosComprados()) {
            System.out.println(ANSI_BOLD + contador + ". " + ANSI_RESET + ingresso);
            contador++;
        }
    }

    private void tornarVIP() {
        fachada.getFachadaCliente().getClienteNegocio().tornarVIP(cliente);
        System.out.println("\n" + ANSI_GREEN + ANSI_BOLD);
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║        VOCÊ AGORA É UM CLIENTE VIP! 👑            ║");
        System.out.println("╚═══════════════════════════════════════════════════╝" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "Parabéns! Agora você tem 35% de desconto na compra de ingressos!" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "AVISO: Seu status como Cliente VIP será atualizado ao realizar novamente seu login!" + ANSI_RESET);
    }

    private void cancelarVIP() {
        fachada.getFachadaCliente().getClienteNegocio().cancelarVIP(cliente);
        System.out.println("\n" + ANSI_GREEN + ANSI_BOLD);
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║        VOCÊ VOLTOU A SER CLIENTE PADRÃO           ║");
        System.out.println("╚═══════════════════════════════════════════════════╝" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "AVISO: Seu status como Cliente Padrão será atualizado ao realizar novamente seu login!" + ANSI_RESET);
    }


}