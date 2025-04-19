package UI;

import fachada.FachadaGerente;
import negocio.exceptions.filmes.FilmeJaEstaNoCatalogoException;
import negocio.exceptions.filmes.FilmeNaoEstaCadastradoException;
import negocio.exceptions.filmes.NenhumFilmeEncontradoException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

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
        String duracao = lerDado("Duração");
        if (duracao == null) return;
        String classificacao = lerDado("Classificação");
        if (classificacao == null) return;

        try {
            fachada.adicionarFilme(nome, genero, duracao, classificacao);
            System.out.println("\033[92m Filme adicionado com Sucesso! \033[0m");
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
            System.out.println("\033[92m Filme removido com Sucesso! \033[0m");
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
        String duracao = lerDado("Duração");
        if (duracao == null) return;
        String classificacao = lerDado("Classificação");
        if (classificacao == null) return;

        try {
            fachada.atualizarFilme(nome, genero, duracao, classificacao);
            System.out.println("\033[92m Filme adicionado com Sucesso! \033[0m");
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
            if(campo.equals("Duração")){
                String duracaoValida = verificarDuracao(dado);
                if (duracaoValida == null){
                    continue;
                }
                return duracaoValida;
            }
            if(campo.equals("Classificação")){
                String classvalida = verificarClassificacao(dado);
                if (classvalida == null){
                    continue;
                }
                return classvalida;
            }

            return dado;
        }

    }

    private String verificarDuracao(String dado){
        if (!dado.matches("\\d{1}h\\d{2}|\\d{1}h")) {
            System.err.println("Formato inválido! Deve ser no formato xhxx ou xh.");
            return null;
        }
        if (dado.matches("\\d{1}h\\d{2}")) {
            String minutosStr = dado.substring(dado.indexOf('h') + 1);
            int minutos = Integer.parseInt(minutosStr);
            if (minutos > 59) {
                System.err.println("Os minutos não podem ser maiores que 59.");
                return null;
            }
        }
        return dado;
    }

    private String verificarClassificacao(String dado) {
        Set<String> classificacoesValidas = new HashSet<>();
        classificacoesValidas.add("Livre");
        classificacoesValidas.add("10");
        classificacoesValidas.add("12");
        classificacoesValidas.add("14");
        classificacoesValidas.add("16");
        classificacoesValidas.add("18");

        if (classificacoesValidas.contains(dado.trim())) {
            return dado.trim();
        }
        System.err.println("Classificação inválida! As opções válidas são: Livre, 10, 12, 14, 16, 18");
        return null;
    }
}
