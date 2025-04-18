package UI;

import fachada.FachadaGerente;

import java.util.Scanner;

public class TelaGerente {
    private FachadaGerente fachadaGerente;
    private Scanner scanner;

    public TelaGerente(FachadaGerente fachadaGerente){
        this.scanner = new Scanner(System.in);
        this.fachadaGerente = fachadaGerente;

    }

    public void iniciar(){

        while(true){
            System.out.println("Tela Gerente");
            System.out.println("1 - Gerenciar Filmes");
            System.out.println("2 - Gerenciar Salas");
            System.out.println("3 - Gerenciar Sessoes");
            System.out.println("4 - Logout");

            String opcao = scanner.nextLine().trim();

            switch (opcao){
                case "1":
                    TelaCadastroFilme telacadastroFilme = new TelaCadastroFilme(fachadaGerente);
                    telacadastroFilme.iniciar();
                    break;
                case "2":
                    TelaGerenciamentodeSalas telaGerenciamentosalas = new TelaGerenciamentodeSalas(fachadaGerente);
                    telaGerenciamentosalas.iniciar();
                    break;
                case "3":
                    TelaGerenciamentoSessoes telaGerenciamentoSessoes = new TelaGerenciamentoSessoes(fachadaGerente);
                    telaGerenciamentoSessoes.iniciar();
                    break;
                case "4":
                    System.out.println("Saindo...");
                    return;
                default:
                    System.err.println("Opção Inválida");
            }
        }

    }
}
