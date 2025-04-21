package UI;

import fachada.FachadaGerente;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import negocio.exceptions.filmes.FilmeNaoEstaCadastradoException;
import negocio.exceptions.sessoes.ValorInvalidoException;
import negocio.exceptions.salas.SalaNaoEncontradaException;
import negocio.exceptions.sessoes.NenhumaSessaoEncontradaException;
import negocio.exceptions.sessoes.SessaoJaExisteException;
import negocio.exceptions.sessoes.SessaoNaoEncontradaException;

public class TelaGerenciamentoSessoes {
    private final Scanner scanner;
    private final FachadaGerente fachada;

    public TelaGerenciamentoSessoes(FachadaGerente fachada) {
        this.scanner = new Scanner(System.in);
        this.fachada = fachada;
    }

    public void iniciar() {
        System.out.println("------------------------------------");
        System.out.println("Tela de Gerenciamento de Sessoes");
        System.out.println("------------------------------------");
        listarSessoes();
        while (true) {
            System.out.println("\n1 - Adicionar sessao");
            System.out.println("2 - Remover sessao");
            System.out.println("3 - Atualizar sessao");
            System.out.println("4 - Buscar sessoes por titulo de filme");
            System.out.println("5 - Listar sessoes pelo dia");
            System.out.println("6 - Listar todas as sessoes");
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
//                    atualizarSessao();
                    break;
                case "4":
                    buscarSessaoporTitulo();
                    break;
                case "5":
                    buscarSessaoDia();
                    break;
                case "6":
                    listarSessoes();
                case "7":
                    System.out.println("Voltando...");
                    return;
                default:
                    System.out.println("Opção Invalida");
            }
        }
    }

    private void listarSessoes(){
        ArrayList<String> sessoes;
        try{
            System.out.println(">>>>> Sessões Cadastradas <<<<<");
            sessoes = fachada.listarTodas();
            for (String sessoe : sessoes) {
                System.out.println(sessoe);
            }
        } catch (NenhumaSessaoEncontradaException e) {
            System.err.println(e.getMessage());
        }
    }

    private void adicionarSessao(){
        String horario,idFilme,idSala,dia;
        double valorIngresso;

        idFilme = lerDado("ID do Filme");
        if (idFilme== null)return;
        try {
            fachada.procurarFilmePorID(idFilme);
        } catch (FilmeNaoEstaCadastradoException e) {
            System.err.println(e.getMessage());
            return;
        }
        idSala = lerDado("ID da Sala");
        if (idSala == null) return;
        try{
            fachada.procurarSala(idSala);
        } catch (SalaNaoEncontradaException e) {
            System.err.println(e.getMessage());
            return;
        }
        horario = lerHorario();
        if (horario== null)return;
        dia = lerData();
        if (dia== null)return;
        
        valorIngresso = lerValorIngresso();
        if(valorIngresso <= 0) return;
        
        try{
            fachada.adicionarSessao(horario,idFilme,idSala,dia,valorIngresso);
            System.out.println("\033[92m Sessão adicionada com Sucesso! \033[0m");
        } catch (FilmeNaoEstaCadastradoException | SalaNaoEncontradaException | SessaoJaExisteException | ValorInvalidoException e) {
            System.err.println(e.getMessage());
        }
    }

    private double lerValorIngresso() {
        while (true) {
            System.out.print("Valor do ingresso: ");
            try {
                String input = scanner.nextLine().replace(",", ".");
                double valor = Double.parseDouble(input);
                
                if (valor <= 0) {
                    System.err.println("O valor do ingresso deve ser maior que zero.");
                    continue;
                }
                
                return valor;
            } catch (NumberFormatException e) {
                System.err.println("Por favor, digite um valor numérico válido.");
            }
        }
    }

    private void removerSessao(){
        String ID;
        ID = lerDado("ID da Sessao");
        try{
            fachada.removerSessao(ID);
            System.out.println("\033[92m Sessão removida com Sucesso! \033[0m");
        } catch (SessaoNaoEncontradaException e) {
            System.err.println(e.getMessage());
        }
    }

//    private void atualizarSessao(){
//        String ID, horario, idFilme, idSala,dia;
//        double valorIngresso;
//
//        ID = lerDado("ID da Sessão");
//        if (sala == null) return;
//        try{
//            fachada.procurarSessaoPorId(ID);
//        } catch (SessaoNaoEncontradaException e) {
//            System.err.println(e.getMessage());
//            return;
//        }
//        horario = lerHorario();
//        if (horario == null) return;
//        dia= lerData();
//        if (dia == null)return;
//        filme = lerDado("Nome do filme que será colocado nesse horário");
//        if (filme==null)return;
//        try {
//            fachada.procurarFilme(filme);
//        } catch (FilmeNaoEstaCadastradoException e) {
//            System.err.println(e.getMessage());
//            return;
//        }
//        System.out.println("valor do Ingresso: ");
//        valorIngresso = scanner.nextDouble();
//        try {
//            fachada.atualizarSessao(horario,filme,sala,dia,valorIngresso);
//            System.out.println("\033[92m Sessão atualizada com Sucesso! \033[0m");
//        } catch (SessaoNaoEncontradaException | FilmeNaoEstaCadastradoException | SalaNaoEncontradaException e) {
//            System.err.println(e.getMessage());
//        }
//    }

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
}

