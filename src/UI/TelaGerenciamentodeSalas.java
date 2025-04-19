package UI;

import fachada.FachadaGerente;
import negocio.exceptions.salas.CodigoSalaJaExisteException;
import negocio.exceptions.salas.LimiteDeSalasExcedidoException;
import negocio.exceptions.salas.NenhumaSalaEncontradaException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


//mt codigo repetido, fazer metodos pra leitura

public class TelaGerenciamentodeSalas {
    private Scanner scanner;
    private FachadaGerente fachada;

    public TelaGerenciamentodeSalas(FachadaGerente fachada){
        this.scanner = new Scanner(System.in);
        this.fachada = fachada;
    }

    public void iniciar(){
        System.out.println("Tela Cadastro Salas");
        while (true){
            System.out.println("1 - Adicionar Sala");
            System.out.println("2 - Remover Sala");
            System.out.println("3 - Listar Salas");
            System.out.println("4 - Voltar");


            String opcao = scanner.nextLine();

            switch (opcao){
                case "1":
                    adicionarSala();
                    break;
                case "2":
                    removerSala();
                    break;
                case "3":
                    listarSalas();
                    break;
                case "4":
                    return;
                default:
                    System.err.println("Opção Inválida");

            }
        }
    }
    private void adicionarSala() {
        System.out.println("(digite 0 a qualquer momento para sair)");
        System.out.println("Código da Sala:");
        String codigo = lerCodigo();
        if (codigo == null) return;
        String tipo;
        while (true) {
            System.out.println("Tipo da Sala (2D/3D)");
            tipo = scanner.nextLine().toUpperCase();

            if (!tipo.equals("2D") && !tipo.equals("3D")) {
                System.err.println("Tipo inválido! Digite 2D ou 3D");
                continue;
            }
            break;
        }
        int linhas, colunas;
        while (true) {
            try {
                System.out.println("Quantidade de linhas de poltronas:");
                linhas = scanner.nextInt();
                scanner.nextLine();
                if (linhas < 0) {
                    System.err.println("Digite um número maior que zero");
                    continue;
                } else if (linhas == 0) {
                    return;
                }
                break;
            } catch (InputMismatchException e) {
                System.err.println("Digite um numero valido");
                scanner.nextLine();
            }
        }
        while (true) {
            try {
                System.out.println("Quantidade de colunas de poltronas:");
                colunas = scanner.nextInt();
                scanner.nextLine();
                if (colunas < 0) {
                    System.err.println("Digite um número maior que zero");
                    continue;
                } else if (colunas == 0) {
                    return;
                }
                break;
            } catch (InputMismatchException e) {
                System.err.println("Digite um numero valido");
                scanner.nextLine();
            }
        }
        try {
            fachada.adicionarSala(codigo, tipo, linhas, colunas);
            System.out.println("Sala adicionada com Sucesso!");
        } catch (CodigoSalaJaExisteException | LimiteDeSalasExcedidoException e) {
            System.err.println(e.getMessage());
        }
    }

    private void removerSala(){
        System.out.println("(digite 0 a qualquer momento para sair)");
        System.out.println("O código da sala que será removida");
        String codigo = lerCodigo();
        if (codigo == null) return;
        try {
            fachada.removerSala(codigo);
            System.out.println("Sala removida com Sucesso!");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    private void listarSalas(){
        try {
            ArrayList<String> salas = fachada.listarSalas();
            for (String sala : salas) {
                System.out.println(sala);
            }
        } catch (NenhumaSalaEncontradaException e) {
            System.err.println("Nenhuma sala encontrada");

        }
    }
    private String lerCodigo(){
        while (true) {
            String dado = scanner.nextLine().trim();

            if (dado.equals("0")) {
                System.out.println("\nOperação cancelada.");
                return null;
            }
            if (dado.isEmpty()) {
                System.err.println("Código não pode ser vazio!");
                continue;
            }
            return dado;
        }
    }

}
