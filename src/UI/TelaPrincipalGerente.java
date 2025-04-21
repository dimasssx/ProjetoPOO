package UI;

import java.util.Scanner;

import dados.IRepositorioClientes;
import dados.RepositorioClientesArquivoBinario;
import fachada.FachadaGerente;

public class TelaPrincipalGerente {
    private final FachadaGerente fachadaGerente;
    private final Scanner scanner;

    public TelaPrincipalGerente(FachadaGerente fachadaGerente){
        this.scanner = new Scanner(System.in);
        this.fachadaGerente = fachadaGerente;

    }

    public void iniciar(){

        while(true){
            System.out.println("------------------------------------");
            System.out.println("Tela Gerente");
            System.out.println("------------------------------------");
            System.out.println("1 - Gerenciar Filmes");
            System.out.println("2 - Gerenciar Salas");
            System.out.println("3 - Gerenciar Sessoes");
            System.out.println("4 - Logout");
            System.out.println("5 remover depois- listar usuarios");


            String opcao = scanner.nextLine().trim();

            switch (opcao){
                case "1":
                    TelaGerenciamentoDeFilmes telaGerenciamentoFilmes = new TelaGerenciamentoDeFilmes(fachadaGerente);
                    telaGerenciamentoFilmes.iniciar();
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
                case "5":
                    IRepositorioClientes repositorioClientes = new RepositorioClientesArquivoBinario();
                    repositorioClientes.imprimir();
                    break;
                default:
                    System.err.println("Opção Inválida");
            }
        }
    }
}
