package UI;

import fachada.FachadaCliente;
import fachada.Movietime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import negocio.entidades.Cliente;
import negocio.entidades.ClienteVIP;
import negocio.exceptions.filmes.FilmeNaoEstaCadastradoException;
import negocio.exceptions.sessoes.SessaoNaoEncontradaException;

public class TelaPrincipalCliente {
    private MonthDay hoje;
    private final FachadaCliente clienteFachada;
    private final Scanner scanner;
    private final Cliente cliente;
    private final Movietime fachadaPrincipal;

    // cores ANSI para melhorar a interface, vao estar presentes apenas nos prints da tela para mudar a cor
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BOLD = "\u001B[1m";

    public TelaPrincipalCliente(FachadaCliente clienteFachada, Cliente cliente, Movietime fachadaPrincipal) {
        this.clienteFachada = clienteFachada;
        scanner = new Scanner(System.in);
        hoje = MonthDay.now();
        this.cliente = cliente;
        this.fachadaPrincipal = fachadaPrincipal;
    }

    public void iniciar() {
        String statusVip = cliente instanceof ClienteVIP ? " 👑 VIP 👑" : "";
        
        System.out.println("\n" + ANSI_BOLD);
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║               MOVIETIME CINEMA               ║");
        System.out.println("╚══════════════════════════════════════════════╝" + ANSI_RESET);
        
        System.out.println("\n" + ANSI_BOLD + "Bem-vindo, " + cliente.getNome().split(" ")[0] + "!" + statusVip + ANSI_RESET);
        System.out.println(ANSI_BOLD + "═════════════════════════════════════════════" + ANSI_RESET);
        
        exibicaoSessoesDeHoje(); //exibe as sessões de hoje

        while(true){
            System.out.println("\n" + ANSI_BOLD + ">>>>> MENU PRINCIPAL <<<<<" + ANSI_RESET);
            System.out.println("1 - " + "Comprar ingresso" + ANSI_RESET);
            System.out.println("2 - " + "Buscar sessões por dia" + ANSI_RESET);
            System.out.println("3 - " + "Buscar sessões por filme" + ANSI_RESET);
            System.out.println("4 - " + "Gerenciamento de conta" + ANSI_RESET);
            System.out.println("5 - " + "Logout" + ANSI_RESET);

            System.out.print("► ");
            String opcao;

            try {
                opcao = scanner.nextLine();
            } catch (Exception e) {
                System.out.println(ANSI_RED + "Digite um número válido!" + ANSI_RESET);
                scanner.nextLine();
                continue;
            }

            switch (opcao){
                case "1":
                    TelaComprarIngresso comprar = new TelaComprarIngresso(clienteFachada,cliente);
                    comprar.iniciar();
                    break;

                case "2":
                    try {
                        buscarporDia();
                    } catch (SessaoNaoEncontradaException e) {
                        System.out.println(ANSI_RED + e.getMessage() + ANSI_RESET);
                    }
                    break;
                case "3":
                    try {
                        buscarPorFilme();
                    }
                    catch (SessaoNaoEncontradaException e){
                        System.out.println(ANSI_RED + e.getMessage() + ANSI_RESET);
                    }
                    break;
                case "4":
                    TelaGerenciamentoDeContaCliente telaGerenciamentoDeContaCliente = new TelaGerenciamentoDeContaCliente(fachadaPrincipal,cliente);
                    telaGerenciamentoDeContaCliente.iniciar();
                    break;
                case "5":
                    System.out.println(ANSI_YELLOW + "Saindo do sistema..." + ANSI_RESET);
                    return;
                default:
                    System.out.println(ANSI_RED + "Opção inválida! Por favor, tente novamente." + ANSI_RESET);
            }
        }
    }

    public void exibicaoSessoesDeHoje() {
        String dataHoje = hoje.format(DateTimeFormatter.ofPattern("dd/MM"));
        
        System.out.println("\n" + ANSI_BOLD);
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║           SESSÕES EM EXIBIÇÃO HOJE           ║");
        System.out.println("║                   " + dataHoje + "                      ║");
        System.out.println("╚══════════════════════════════════════════════╝" + ANSI_RESET);

        try {
            ArrayList<String> sessoesFormatadas = clienteFachada.procurarSessoesHoje();

            if (sessoesFormatadas.isEmpty()) {
                System.out.println(ANSI_YELLOW + "Nenhuma sessão disponível para hoje." + ANSI_RESET);
            } else {
                for (String sessao : sessoesFormatadas) {
                    System.out.println(sessao);
                }
            }
        } catch (SessaoNaoEncontradaException e) {
            System.out.println(ANSI_YELLOW + "Nenhuma sessão encontrada para hoje." + ANSI_RESET);
        }
    }

    public void buscarporDia() throws SessaoNaoEncontradaException {
        System.out.println("\n" + ANSI_BOLD);
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║             BUSCAR SESSÕES POR DIA           ║");
        System.out.println("╚══════════════════════════════════════════════╝" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "Digite 0 a qualquer momento para cancelar" + ANSI_RESET);
        
        String dataInput = lerData();
        if (dataInput == null) return;

        try {
            ArrayList<String> sessoesFormatadas = clienteFachada.procurarSessaoporDia(dataInput);

            if (sessoesFormatadas.isEmpty()) {
                System.out.println(ANSI_YELLOW + "Nenhuma sessão encontrada para esta data." + ANSI_RESET);
            } else {
                System.out.println("\n" + ANSI_BOLD);
                System.out.println("╔══════════════════════════════════════════════╗");
                System.out.println("║           SESSÕES PARA O DIA: " + dataInput + "          ║");
                System.out.println("╚══════════════════════════════════════════════╝" + ANSI_RESET);
                
                for (String sessao : sessoesFormatadas) {
                    System.out.println(sessao);
                }
            }

        } catch (SessaoNaoEncontradaException e) {
            System.out.println(ANSI_RED + e.getMessage() + ANSI_RESET);
        }
    }


    public void buscarPorFilme() throws SessaoNaoEncontradaException {
        System.out.println("\n" + ANSI_BOLD);
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║            BUSCAR SESSÕES POR FILME          ║");
        System.out.println("╚══════════════════════════════════════════════╝" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "Digite 0 a qualquer momento para cancelar" + ANSI_RESET);
        
        String tituloInput = lerTituloFilme();
        if (tituloInput == null) return;

        try {
            clienteFachada.consultarFilme(tituloInput); // validação do catálogo

            ArrayList<String> sessoesFormatadas = clienteFachada.procurarSessaoPorTituloDoFilme(tituloInput);

            if (sessoesFormatadas.isEmpty()) {
                System.out.println(ANSI_YELLOW + "Nenhuma sessão encontrada para o filme: " + tituloInput + ANSI_RESET);
            } else {
                System.out.println("\n" + ANSI_BOLD);
                System.out.println("╔══════════════════════════════════════════════╗");
                System.out.println("║           SESSÕES PARA O FILME:              ║");
                System.out.println("║                    " + tituloInput + "                       ║");
                System.out.println("╚══════════════════════════════════════════════╝" + ANSI_RESET);
                
                for (String sessao : sessoesFormatadas) {
                    System.out.println(sessao);
                }
            }
        } catch (FilmeNaoEstaCadastradoException | SessaoNaoEncontradaException e) {
            System.out.println(ANSI_RED + e.getMessage() + ANSI_RESET);
        }
    }

    private String lerData() {
        String data;
        do {
            System.out.print(ANSI_BOLD + "Digite a data da sessão (dd-MM): " + ANSI_RESET);
            data = scanner.nextLine().trim();

            if (data.equals("0")) {
                System.out.println(ANSI_YELLOW + "Operação cancelada." + ANSI_RESET);
                return null;
            }

            if (!isDataValida(data)) {
                System.out.println(ANSI_RED + "Formato inválido! Use dd-MM (ex: 17-04)." + ANSI_RESET);
            }
        } while (!isDataValida(data));

        return data;
    }

    private boolean isDataValida(String data) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM");
            MonthDay.parse(data, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String lerTituloFilme() {
        String titulo;
        do {
            System.out.print(ANSI_BOLD + "Digite o nome do Filme: " + ANSI_RESET);
            titulo = scanner.nextLine().trim();

            if (titulo.equals("0")) {
                System.out.println(ANSI_YELLOW + "Operação cancelada." + ANSI_RESET);
                return null;
            }

            if (titulo.isEmpty()) {
                System.out.println(ANSI_RED + "O título não pode estar vazio!" + ANSI_RESET);
            }

        } while (titulo.isEmpty());

        return titulo;
    }
}