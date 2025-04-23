package UI;

import fachada.FachadaCliente;
import java.util.ArrayList;
import java.util.Scanner;
import negocio.entidades.Assento;
import negocio.entidades.Ingresso;
import negocio.entidades.Sessao;
import negocio.exceptions.assentos.AssentoOcupadoException;

public class TelaEscolhadeAssentos {
    private final FachadaCliente fachada;
    private final Scanner scanner;
    private final Sessao sessao;
    private ArrayList<Ingresso> ingressosSelecionados;
    private ArrayList<String> assentosSelecionados;

    // cores ANSI para melhorar a interface, vao estar presentes nos prints da tela para mudar a cor
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_BOLD = "\u001B[1m";

    public TelaEscolhadeAssentos(FachadaCliente fachada, Sessao sessao){
        this.fachada = fachada;
        this.sessao = sessao;
        this.scanner = new Scanner(System.in);
        this.ingressosSelecionados = new ArrayList<>();
        this.assentosSelecionados = new ArrayList<>(); // Inicializa a lista de controle
    }
    public TelaEscolhadeAssentos(FachadaCliente fachada, Sessao sessao,Scanner scanner){
        this.fachada = fachada;
        this.sessao = sessao;
        this.scanner = scanner;
        this.ingressosSelecionados = new ArrayList<>();
        this.assentosSelecionados = new ArrayList<>(); // Inicializa a lista de controle
    }

    public ArrayList<Ingresso> iniciar(int quantidadeIngressos){
        if (quantidadeIngressos <= 0) {
            return null;
        }
        
        imprimirCabecalho();

        for (int i = 0; i < quantidadeIngressos; i++) {
            System.out.println("\n" + ANSI_BOLD + "═════════════════════════════════════════" + ANSI_RESET);
            System.out.println(ANSI_BOLD + "  ESCOLHA DO ASSENTO " + (i+1) + " DE " + quantidadeIngressos + ANSI_RESET);
            System.out.println(ANSI_BOLD + "═════════════════════════════════════════" + ANSI_RESET);
            
            visualizarAssentosDaSessao(sessao);
            
            Ingresso ingresso = null;
            try {
                ingresso = escolherAssento();
            } catch (AssentoOcupadoException e) {
                System.err.println(ANSI_RED + "► " + e.getMessage() + ANSI_RESET);
                i--;
                continue;
            }
            
            if (ingresso == null) {
                System.out.println(ANSI_YELLOW + "Operação de escolha de assento cancelada." + ANSI_RESET);
                return null;
            }
            
            ingressosSelecionados.add(ingresso);
            System.out.println(ANSI_GREEN + "✓ Assento selecionado com sucesso!" + ANSI_RESET);
        }
        
        return ingressosSelecionados;
    }
    
    private void imprimirCabecalho() {
        System.out.println("\n" + ANSI_BOLD);
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║              SELEÇÃO DE ASSENTOS                  ║");
        System.out.println("╚═══════════════════════════════════════════════════╝" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "Digite 0 a qualquer momento para cancelar a operação." + ANSI_RESET);
    }
    
    private Ingresso escolherAssento() throws AssentoOcupadoException {
        while (true) {
            System.out.println("\n" + ANSI_BOLD + "Digite o assento desejado (ex: A1):" + ANSI_RESET);
            System.out.print("► ");
            String assentoInput = scanner.nextLine().toUpperCase().trim();
            
            if (assentoInput.equals("0")) {
                return null;
            }
            
            try {
                if (assentoInput.length() < 2 || !Character.isLetter(assentoInput.charAt(0))) {
                    System.out.println(ANSI_RED + "✗ Formato de assento inválido. Use (ex: A1)" + ANSI_RESET);
                    continue;
                }

                if (assentosSelecionados.contains(assentoInput)) {
                    throw new AssentoOcupadoException("Este assento já foi selecionado nesta compra. Escolha outro assento.");
                }
                
                int fileira = assentoInput.charAt(0) - 'A';
                int poltrona = Integer.parseInt(assentoInput.substring(1)) - 1;
                
                Assento assento = sessao.getAssento(fileira, poltrona);
                
                if (assento == null) {
                    System.out.println(ANSI_RED + "✗ Assento inexistente!" + ANSI_RESET);
                    continue;
                }
                
                if (assento.isReservado()) {
                    throw new AssentoOcupadoException("Este assento já está reservado!");
                }
                
                // Solicitar o tipo de ingresso (Meia ou Inteira)
                String tipoIngresso = escolherTipoIngresso();
                if (tipoIngresso == null) {
                    return null; // Operação cancelada
                }

                assentosSelecionados.add(assentoInput);

                return new Ingresso(sessao, assento, tipoIngresso);
                
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED + "✗ Formato de assento inválido. Use (ex: A1)" + ANSI_RESET);
            }
        }
    }
    
    private String escolherTipoIngresso() {
        System.out.println("\n" + ANSI_BOLD + "═════════════════════════════════════════" + ANSI_RESET);
        System.out.println(ANSI_BOLD + "  ESCOLHA DO TIPO DE INGRESSO" + ANSI_RESET);
        System.out.println(ANSI_BOLD + "═════════════════════════════════════════" + ANSI_RESET);
        System.out.println("1 - Inteira");
        System.out.println("2 - Meia (Apresente seu documento no estabelecimento para entrar na sessão)");
        System.out.println("0 - Cancelar");
        
        while (true) {
            System.out.print("► ");
            String opcao = scanner.nextLine().trim();
            
            switch (opcao) {
                case "1":
                    return "Inteira";
                case "2":
                    return "Meia";
                case "0":
                    System.out.println(ANSI_YELLOW + "Operação cancelada." + ANSI_RESET);
                    return null;
                default:
                    System.out.println(ANSI_RED + "Opção inválida. Por favor, escolha 1 para Inteira, 2 para Meia ou 0 para cancelar." + ANSI_RESET);
            }
        }
    }

    private void visualizarAssentosDaSessao(Sessao sessao){
        Assento[][] assentos = sessao.getAssentos();

        System.out.println("\n" + ANSI_BOLD + "        ▼▼▼ TELA DO CINEMA ▼▼▼" + ANSI_RESET);
        System.out.println(ANSI_BOLD + "   ┌────────────────────────────┐" + ANSI_RESET);
        System.out.println(ANSI_BOLD + "   └────────────────────────────┘" + ANSI_RESET + "\n");
        
        // Mapa de assentos
        for (int i = 0; i < assentos.length; i++) {
            System.out.print(ANSI_BOLD + (char) ('A' + i) + " " + ANSI_RESET);
            for (int j = 0; j < assentos[i].length; j++) {
                String posicao = String.valueOf((char)('A' + i)) + (j + 1);
                if (assentosSelecionados.contains(posicao)) {
                    System.out.print(ANSI_GREEN + "[S] " + ANSI_RESET); // Assento selecionado na compra atual
                } else if (assentos[i][j].isReservado()) {
                    System.out.print(ANSI_RED + "[X] " + ANSI_RESET); // Assento já reservado
                } else {
                    System.out.print(ANSI_BLUE + "[ ] " + ANSI_RESET); // Assento disponível
                }
            }
            System.out.println();
        }

        System.out.print("   ");
        for (int j = 0; j < assentos[0].length; j++) {
            System.out.print(ANSI_BOLD + (j + 1) + "   " + ANSI_RESET);
        }

        System.out.println("\n\n" + ANSI_BOLD + "Legenda: " + ANSI_RESET + 
                           ANSI_BLUE + "[ ] " + ANSI_RESET + "Disponível | " + 
                           ANSI_RED + "[X] " + ANSI_RESET + "Reservado | " + 
                           ANSI_GREEN + "[S] " + ANSI_RESET + "Selecionado na compra atual");
    }
}
