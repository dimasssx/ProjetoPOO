package UI;

import fachada.FachadaGerente;
import negocio.exceptions.FilmeJaEstaNoCatalogoException;
import negocio.exceptions.FilmeNaoEstaCadastradoException;
import negocio.exceptions.NenhumFilmeEncontradoException;
import java.util.ArrayList;
import java.util.Scanner;

public class TelaCadastroFilme {
    private Scanner scanner;
    private FachadaGerente fachada;

    public TelaCadastroFilme(FachadaGerente fachada) {
        this.scanner = new Scanner(System.in);
        this.fachada = fachada;
    }

    public void iniciar() {
        System.out.println("Tela Cadastro Filmes");
        while (true) {
            System.out.println("1 - Adicionar Filme");
            System.out.println("2 - Remover Filme");
            System.out.println("3 - Atualizar Filme");
            System.out.println("4 - Buscar Filme");
            System.out.println("5 - Listar Filmes");
            System.out.println("6 - Voltar");

            String opcao;
            opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    adicionarFilme();
                    break;
                case "2":
                    removerFilme();
                    break;
                case "3":
                    atualizarFilme();
                    break;
                case "4":
                    buscarFilme();
                    break;
                case "5":
                    listarFilmes();
                    break;
                case "6":
                    return;

            }
        }
    }

    private void adicionarFilme() {
        System.out.println("Nome do Filme:");
        String nome = scanner.nextLine();
        System.out.println("Genero: ");
        String genero = scanner.nextLine();
        System.out.println("Duracao: ");
        String duracao = scanner.nextLine();
        System.out.println("Classificacao: ");
        String classificacao = scanner.nextLine();
        try {
            fachada.adicionarFilme(nome, genero, duracao, classificacao);
        } catch (FilmeJaEstaNoCatalogoException e) {
            System.err.println(e.getMessage());

        }
    }

    private void removerFilme() {
        System.out.println("Nome do filme que será removido");
        String nome = scanner.nextLine();
        try {
            fachada.removerFilme(nome);
        } catch (FilmeNaoEstaCadastradoException e) {
            System.err.println(e.getMessage());
        }
    }

    private void atualizarFilme() {
        System.out.println("Nome");
        String nome = scanner.nextLine();

        try{
            fachada.procurarFilme(nome);
        } catch (FilmeNaoEstaCadastradoException e) {
            System.err.println(e.getMessage());;
        }
        System.out.println("Genero");
        String genero = scanner.nextLine();
        System.out.println("Duracao");
        String duracao = scanner.nextLine();
        System.out.println("Classificacao");
        String classificacao = scanner.nextLine();

        try {
            fachada.atualizarFilme(nome, genero, duracao, classificacao);
        } catch (FilmeNaoEstaCadastradoException e) {
            System.err.println(e.getMessage());
        }

    }

    private void buscarFilme() {
        System.out.println("Nome do Filme");
        String nome = scanner.nextLine();
        try {
            System.out.println(fachada.procurarFilme(nome));
        } catch (FilmeNaoEstaCadastradoException e) {
            System.err.println(e.getMessage());
        }
    }

    private void listarFilmes(){
        try {
            ArrayList<String> filmes = fachada.imprimirCatalogo();
            System.out.println("=== Catálogo de Filmes ===");
            for (String filme : filmes) {
                System.out.println(filme);
            }
        } catch (NenhumFilmeEncontradoException e) {
            System.err.println(e.getMessage());
        }
    }
}
