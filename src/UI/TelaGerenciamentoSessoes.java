package UI;

import fachada.FachadaGerente;

import java.util.Scanner;

public class TelaGerenciamentoSessoes {
    private Scanner scanner;
    private FachadaGerente fachada;

    public TelaGerenciamentoSessoes(FachadaGerente fachada) {
        this.scanner = new Scanner(System.in);
        this.fachada = fachada;
    }

    public void iniciar() {
        System.out.println("Tela Cadastro Sessoes");

        while (true) {
            System.out.println("1 - Adicionar Sessao");
            System.out.println("2 - Remover Sessao");
            System.out.println("3 - Atualizar Sessao");
            System.out.println("4 - Buscar Sessoes por titulo de Filme");
            System.out.println("5 - Listar Sessoes do dia");
            System.out.println("6 - Listar Todas as Sessoes");
            System.out.println("7 - Voltar");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    adicionarSessao();
                    break;
                case "2":
                    removerSessao();
                    break;
                case "3":
                    atualizarSessoes();
                    break;
                case "4":
                    buscarSessaoporTitulo();
                    break;
                case "5":
                    buscarSessaoDia();
                    break;
                case "6":
                    imprimeTodasSessoes();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Opção Invalida");

            }
        }
    }
    private void adicionarSessao(){

    }
}
