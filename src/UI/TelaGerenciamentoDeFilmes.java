package UI;

import fachada.FachadaGerente;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import negocio.exceptions.filmes.FilmeJaEstaNoCatalogoException;
import negocio.exceptions.filmes.FilmeNaoEstaCadastradoException;
import negocio.exceptions.filmes.NenhumFilmeEncontradoException;
import static UI.Utils.ValidacaoEntradas.*;

public class TelaGerenciamentoDeFilmes {
    private final Scanner scanner;
    private final FachadaGerente fachada;

    public TelaGerenciamentoDeFilmes(FachadaGerente fachada) {
        this.scanner = new Scanner(System.in);
        this.fachada = fachada;
    }

    public void iniciar() {
        System.out.println("------------------------------------");
        System.out.println("Tela de Gerenciamento de Filmes");
        System.out.println("------------------------------------");
        listarFilmes();
        while (true) {
            System.out.println("\n1 - Adicionar filme");
            System.out.println("2 - Remover filme");
            System.out.println("3 - Atualizar filme");
            System.out.println("4 - Buscar filme");
            System.out.println("5 - Listar todos os filmes");
            System.out.println("6 - Voltar");

            String opcao=scanner.nextLine();

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
        String ID = lerDado("ID do Filme que será removido");
        if (ID == null) return;
        try {
            fachada.removerFilme(ID);
            System.out.println("\033[92m Filme removido com Sucesso, as sessões vinculadas ao filme, também foram removidas! \033[0m");
        } catch (FilmeNaoEstaCadastradoException e) {
            System.err.println(e.getMessage());
        }
    }
    private void atualizarFilme() {
        System.out.println("(digite 0 a qualquer momento para sair)");
        String ID = lerDado("ID do Filme que deseja modificar");
        if (ID == null) return;
        
        try{
            fachada.procurarFilmePorID(ID);
        } catch (FilmeNaoEstaCadastradoException e) {
            System.err.println(e.getMessage());
            return;
        }
        
        System.out.println(">>>>> Novas caracteristicas do filme <<<<<");
        String nome = lerDado("Nome do Filme");
        if (nome == null) return;
        String genero = lerDado("Gênero");
        if (genero == null) return;
        String duracao = lerDado("Duração");
        if (duracao == null) return;
        String classificacao = lerDado("Classificação");
        if (classificacao == null) return;

        try {
            fachada.atualizarFilmePorID(ID, nome, genero, duracao, classificacao);
            System.out.println("\033[92m Filme atualizado com Sucesso! \033[0m");
        } catch (FilmeNaoEstaCadastradoException e) {
            System.err.println(e.getMessage());
        }

    }
    private void buscarFilme() {
        System.out.println("(digite 0 a qualquer momento para sair)");
        String nome = lerDado("Nome do Filme");
        if (nome == null) return;
        try {
            System.out.println(fachada.procurarFilmePorTitulo(nome));
        } catch (FilmeNaoEstaCadastradoException e) {
            System.err.println(e.getMessage());
        }
    }
    private void listarFilmes(){
        try {
            System.out.println(">>>>> Filmes Cadastrados <<<<<");
            ArrayList<String> filmes = fachada.imprimirCatalogo();
            for (String filme : filmes) {
                System.out.println(filme);
            }
        } catch (NenhumFilmeEncontradoException e) {
            System.err.println(e.getMessage());
        }
    }


}
