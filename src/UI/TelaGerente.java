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

            int opcao = -1;

            try {
                opcao = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.err.println("Digite um número");
                scanner.nextLine();
                continue;
            }

            switch (opcao){
                case 1:
//                    TelaCadastroFilme telacadastro = new TelaCadastroFilme(fachadaGerente);
//                    telacadastro.iniciar();
                    break;
                case 2:
//                    TelaCadastroSalas telaCadastrosala = new TelaCadastroSalas(fachadaGerente);
//                    telaCadastrosala.iniciar();
                    break;
                case 3:
//                    TelaCadastroSessoes telacadastrosessoes = new TelaCadastroSessoes(fachadaGerente);
//                    telacadastrosessoes.iniciar();
                    break;
                case 4:
                    System.out.println("Saindo...");
                    return;
                default:
                    System.out.println("Opção Inválida");
            }


        }

    }
}
