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
                    System.out.println("Voltando...");
                    return;
                default:
                    System.err.println("Opção Invalida");

            }
        }
    }

    private void adicionarFilme() {
        System.out.println("Detalhes do filme que será adicionado ");
        System.out.println("(digite 0 a qualquer momento para sair)");

        String nome = lerDado("Nome do Filme");
        if (nome == null) return;
        String genero = lerDado("Gênero");
        if (genero == null) return;
        String duracao = lerDado("Duração ");
        if (duracao == null) return;
        String classificacao = lerDado("Classificação");
        if (classificacao == null) return;

        try {
            fachada.adicionarFilme(nome, genero, duracao, classificacao);
        } catch (FilmeJaEstaNoCatalogoException e) {
            System.err.println(e.getMessage());

        }
    }

    private void removerFilme() {
        System.out.println("(digite 0 a qualquer momento para sair)");
        String nome = lerDado("Nome do Filme que será removido");
        if (nome == null) return;
        try {
            fachada.removerFilme(nome);
        } catch (FilmeNaoEstaCadastradoException e) {
            System.err.println(e.getMessage());
        }
    }

    private void atualizarFilme() {

        System.out.println("(digite 0 a qualquer momento para sair)");
        String nome = lerDado("Nome do Filme que deseja modificar");
        if (nome == null) return;
        try{
            fachada.procurarFilme(nome);
        } catch (FilmeNaoEstaCadastradoException e) {
            System.err.println(e.getMessage());
            return;
        }
        System.out.println("Caracteristicas que serão atualizadas");
        String genero = lerDado("Gênero");
        if (genero == null) return;
        String duracao = lerDado("Duração ");
        if (duracao == null) return;
        String classificacao = lerDado("Classificação");
        if (classificacao == null) return;

        try {
            fachada.atualizarFilme(nome, genero, duracao, classificacao);
        } catch (FilmeNaoEstaCadastradoException e) {
            System.err.println(e.getMessage());
        }

    }

    private void buscarFilme() {
        System.out.println("(digite 0 a qualquer momento para sair)");
        String nome = lerDado("Nome do Filme");
        if (nome == null) return;
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
    private String lerDado(String campo) {
        System.out.print(campo + ": ");
        while(true){
            String dado = scanner.nextLine().trim();

            if (dado.equals("0")) {
                System.out.println("Operação cancelada.");
                return null;
            }
            if (dado.isEmpty()) {
                System.err.println(campo + " não pode ser vazio!");
                continue;
            }
            return dado;
        }

    }
}
