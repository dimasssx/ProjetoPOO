package UI;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import fachada.FachadaGerente;
import negocio.exceptions.salas.CodigoSalaJaExisteException;
import negocio.exceptions.salas.LimiteDeSalasExcedidoException;
import negocio.exceptions.salas.NenhumaSalaEncontradaException;
import static UI.Utils.ValidacaoEntradas.*;

public class TelaGerenciamentodeSalas {
    private final Scanner scanner;
    private final FachadaGerente fachada;

    public TelaGerenciamentodeSalas(FachadaGerente fachada){
        this.scanner = new Scanner(System.in);
        this.fachada = fachada;
    }

    public void iniciar(){
        System.out.println("------------------------------------");
        System.out.println("Tela de Gerenciamento de Salas");
        System.out.println("------------------------------------");
        listarSalas();
        while (true){

            System.out.println("\n1 - Adicionar sala");
            System.out.println("2 - Remover sala");
            System.out.println("3 - Listar todas as salas");
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
                    System.out.println("Voltando...");
                    return;
                default:
                    System.err.println("Opção Inválida");

            }
        }
    }

    private void adicionarSala() {
        System.out.println("(Digite 0 a qualquer momento para sair)");
        System.out.println("AVISO: Lembre-se que só possuímos espaço físico para DUAS salas 2D e UMA sala 3D");
        String codigo = lerDado("Código da Sala: (Sala 1/ Sala 2/ Sala 3)");
        if (codigo == null) return;
        String tipo;
        while (true) {
            System.out.println("Tipo da Sala: (2D/3D)");
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
                System.out.println("Quantidade de fileiras de poltronas na sala:");
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
                System.out.println("Quantidade de poltronas por fileiras na sala:");
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
            System.out.println("\033[92m Sala adicionada com Sucesso! \033[0m");
        } catch (CodigoSalaJaExisteException | LimiteDeSalasExcedidoException e) {
            System.err.println(e.getMessage());
        }
    }
    private void removerSala(){
        System.out.println("(Digite 0 a qualquer momento para sair)");
        String ID = lerDado("ID da sala que será removida");
        if (ID == null) return;
        try {
            fachada.removerSala(ID);
            System.out.println("\033[92m Sala removida com Sucesso, as sessões vinculadas a sala, também foram removidas! \033[0m");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    private void listarSalas(){
        try {
            System.out.println(">>>>> Salas Cadastradas <<<<<");
            ArrayList<String> salas = fachada.listarSalas();
            for (String sala : salas) {
                System.out.println(sala);
            }
        } catch (NenhumaSalaEncontradaException e) {
            System.err.println("Nenhuma sala encontrada");

        }
    }

}
