package UI;

import fachada.FachadaGerente;
import negocio.exceptions.*;

import java.time.LocalTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
                    atualizarSessao();
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
                case "7":
                    return;
                default:
                    System.out.println("Opção Invalida");
            }
        }
    }
    private void adicionarSessao(){
        String horario,filme,sala,dia;

        filme = lerDado("Nome do Filme");
        if (filme== null)return;
        try {
            fachada.procurarFilme(filme);
        } catch (FilmeNaoEstaCadastradoException e) {
            System.err.println(e.getMessage());
            return;
        }
        sala = lerDado("Código da Sala");
        if (sala == null) return;
        try{
            fachada.procuraSala(sala);
        } catch (SalaNaoEncontradaException e) {
            System.err.println(e.getMessage());
            return;
        }
        horario = lerHorario();
        if (horario== null)return;
        dia = lerData();
        if (dia== null)return;

        try{
            fachada.adicionarSessao(horario,filme,sala,dia);
        } catch (FilmeNaoEstaCadastradoException | SalaNaoEncontradaException | SessaoJaExisteException e) {
            System.err.println(e.getMessage());
        }
    }

    private void removerSessao(){
        String horario,sala,dia;


        sala = lerDado("Codigo da sala");
        if (sala == null) return;
        try{
            fachada.procuraSala(sala);
        } catch (SalaNaoEncontradaException e) {
            System.err.println(e.getMessage());
            return;
        }
        horario = lerHorario();
        if (horario ==null) return;
        dia = lerData();
        if (dia==null) return;
        try{
            fachada.removerSessao(horario,sala,dia);
        } catch (SessaoNaoEncontradaException e) {
            System.err.println(e.getMessage());
        }
    }

    private void atualizarSessao(){
        String horario,filme,sala,dia;


        sala = lerDado("Código da sala");
        if (sala == null) return;
        try{
            fachada.procuraSala(sala);
        } catch (SalaNaoEncontradaException e) {
            System.err.println(e.getMessage());
            return;
        }
        horario = lerHorario();
        if (horario == null) return;
        dia= lerData();
        if (dia == null)return;
        filme = lerDado("Nome do filme que será colocado nesse horário");
        if (filme==null)return;
        try {
            fachada.procurarFilme(filme);
        } catch (FilmeNaoEstaCadastradoException e) {
            System.err.println(e.getMessage());
            return;
        }
        try {
            fachada.atualizarSessao(horario,filme,sala,dia);
        } catch (SessaoNaoEncontradaException | FilmeNaoEstaCadastradoException | SalaNaoEncontradaException e) {
            System.err.println(e.getMessage());
        }
    }

    private void buscarSessaoporTitulo(){
        String filme = lerDado("Nome do Filme");
        if (filme==null)return;
        ArrayList<String> sessoes;
        try {
            sessoes = fachada.procurarSessaoTitulo(filme);
            for (String sessoe : sessoes) {
                System.out.println(sessoe);
            }
        } catch (SessaoNaoEncontradaException e) {
            System.err.println(e.getMessage());
        }
    }

    private void buscarSessaoDia(){
        String dia = lerData();
        if (dia==null)return;
        ArrayList<String> sessoes;
        try{
            sessoes = fachada.procurarSessaoporDia(dia);
            for (String sessoe : sessoes) {
                System.out.println(sessoe);
            }
        } catch (SessaoNaoEncontradaException e) {
            System.err.println(e.getMessage());
        }
    }

    private void imprimeTodasSessoes(){
        ArrayList<String> sessoes;
        try{
            sessoes = fachada.listarTodas();
            for (String sessoe : sessoes) {
                System.out.println(sessoe);
            }
        } catch (NenhumaSessaoEncontradaException e) {
            System.err.println(e.getMessage());
        }
    }


    private String lerHorario() {
        String horario;
        do {
            System.out.println("Horário da sessão (HH:mm):");
            horario = scanner.nextLine();
            if(horario.equals("0")){
                System.out.println("Operação cancelada.");
                return null;
            }
            if (!isHorarioValido(horario)) {
                System.err.println("Horário inválido! Use formato HH:mm");
            }

        } while (!isHorarioValido(horario));

        return horario;
    }
    private boolean isHorarioValido(String horario) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime.parse(horario, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    private String lerData() {
        String data;
        do {
            System.out.println("Data da sessão (dd-MM):");
            data = scanner.nextLine();
            if (data.equals("0")) {
                System.out.println("Operação cancelada.");
                return null;
            }

            if (!isDataValida(data)) {
                System.err.println("Data inválida! Use formato dd/MM");
            }

        } while (!isDataValida(data));

        return data;
    }
    private boolean isDataValida(String data) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM");
            MonthDay.parse(data, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
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

