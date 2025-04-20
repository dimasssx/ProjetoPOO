package UI;

import fachada.FachadaCliente;
import java.util.ArrayList;
import java.util.Scanner;
import negocio.entidades.Assento;
import negocio.entidades.Ingresso;
import negocio.entidades.Sessao;
import negocio.exceptions.assentos.AssentoOcupadoException;

public class TelaEscolhadeAssentos {
    private FachadaCliente fachada;
    private Scanner scanner;
    private Sessao sessao;
    private ArrayList<Ingresso> ingressosSelecionados;
    private ArrayList<String> assentosSelecionados; // Lista para controlar assentos já selecionados nesta compra

    public TelaEscolhadeAssentos(FachadaCliente fachada, Sessao sessao){
        this.fachada = fachada;
        this.sessao = sessao;
        this.scanner = new Scanner(System.in);
        this.ingressosSelecionados = new ArrayList<>();
        this.assentosSelecionados = new ArrayList<>(); // Inicializa a lista de controle
    }

    /**
     * Inicia o processo de escolha de assentos
     * @param quantidadeIngressos quantidade de ingressos a serem selecionados
     * @return ArrayList com os ingressos selecionados ou null se cancelado
     */
    public ArrayList<Ingresso> iniciar(int quantidadeIngressos){
        if (quantidadeIngressos <= 0) {
            return null;
        }
        
        // Loop para escolher os assentos
        for (int i = 0; i < quantidadeIngressos; i++) {
            System.out.println("\nEscolha do assento " + (i+1) + " de " + quantidadeIngressos);
            visualizarAssentosDaSessao(sessao);
            
            Ingresso ingresso = null;
            try {
                ingresso = escolherAssento();
            } catch (AssentoOcupadoException e) {
                System.err.println(e.getMessage());
                i--; // Decrementar o contador para tentar selecionar este assento novamente
                continue;
            }
            
            if (ingresso == null) {
                System.out.println("Operação de escolha de assento cancelada.");
                return null; // Se o usuário cancelar, interrompe todo o processo
            }
            
            ingressosSelecionados.add(ingresso);
        }
        
        return ingressosSelecionados;
    }
    
    private Ingresso escolherAssento() throws AssentoOcupadoException {
        while (true) {
            System.out.println("Digite o assento desejado (ex: A1):");
            String assentoInput = scanner.nextLine().toUpperCase().trim();
            
            if (assentoInput.equals("0")) {
                return null; // Cancela a operação
            }
            
            try {
                // Validar input do assento (formato)
                if (assentoInput.length() < 2 || !Character.isLetter(assentoInput.charAt(0))) {
                    System.err.println("Formato de assento inválido. Use letra+número (ex: A1)");
                    continue;
                }
                
                // Verificar se este assento já foi selecionado nesta mesma compra
                if (assentosSelecionados.contains(assentoInput)) {
                    throw new AssentoOcupadoException("Este assento já foi selecionado nesta compra. Escolha outro assento.");
                }
                
                int fileira = assentoInput.charAt(0) - 'A';
                int poltrona = Integer.parseInt(assentoInput.substring(1)) - 1;
                
                Assento assento = sessao.getAssento(fileira, poltrona);
                
                if (assento == null) {
                    System.err.println("Assento inexistente!");
                    continue;
                }
                
                if (assento.isReservado()) {
                    throw new AssentoOcupadoException("Este assento já está reservado!");
                }
                
                // Adiciona o assento à lista de assentos selecionados nesta compra
                assentosSelecionados.add(assentoInput);
                
                // Criar um ingresso (ainda não finalizado)
                return new Ingresso(sessao, assento);
                
            } catch (NumberFormatException e) {
                System.err.println("Formato de assento inválido. Use letra+número (ex: A1)");
            }
        }
    }

    private void visualizarAssentosDaSessao(Sessao sessao){
        Assento[][] assentos = sessao.getAssentos();
        System.out.println("Mapa de assentos - " + sessao.getFilme().getTitulo() + " às " + sessao.getHorario() + " (" + sessao.getDiaFormatado() + ")");
        
        for (int i = 0; i < assentos.length; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < assentos[i].length; j++) {
                String posicao = String.valueOf((char)('A' + i)) + (j + 1);
                if (assentosSelecionados.contains(posicao)) {
                    System.out.print("[S] "); // Assento selecionado na compra atual
                } else if (assentos[i][j].isReservado()) {
                    System.out.print("[X] "); // Assento já reservado
                } else {
                    System.out.print("[ ] "); // Assento disponível
                }
            }
            System.out.println();
        }
        
        System.out.print("   ");
        for (int j = 0; j < assentos[0].length; j++) {
            System.out.print((j + 1) + "   ");
        }
        System.out.println("\nLegenda: [ ] disponível | [X] reservado | [S] selecionado na compra atual");
    }
}
