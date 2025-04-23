package UI;

import fachada.FachadaGerente;
import java.util.ArrayList;
import java.util.Scanner;

import negocio.exceptions.assentos.AssentoIndisponivelException;
import negocio.exceptions.filmes.FilmeNaoEstaCadastradoException;
import negocio.exceptions.sessoes.*;
import negocio.exceptions.salas.SalaNaoEncontradaException;

import static UI.Utils.ValidacaoEntradas.*;

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
            System.out.println("7 - Reservar sessão para entidade ou evento");
            System.out.println("8 - Voltar");

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
                    listarSessoes();
                    break;
                case "7":
                    reservarSessaoInteira();
                    break;
                case "8":
                    System.out.println("Voltando...");
                    return;
                default:
                    System.out.println("Opção Invalida");
            }
        }
    }

    private void adicionarSessao(){
        System.out.println("(Digite 0 a qualquer momento para sair)");
        String horario,idFilme,idSala,dia;
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
        
        try{
            fachada.adicionarSessao(horario,idFilme,idSala,dia);
            System.out.println("\033[92m Sessão adicionada com Sucesso! \033[0m");
        } catch (FilmeNaoEstaCadastradoException | SalaNaoEncontradaException | SessaoJaExisteException | ValorInvalidoException |ConflitoHorarioException e) {
            System.err.println(e.getMessage());
        }
    }
    private void removerSessao(){
        System.out.println("(Digite 0 a qualquer momento para sair)");
        String ID;
        ID = lerDado("ID da Sessao");
        if (ID == null) return;
        try{
            fachada.removerSessao(ID);
            System.out.println("\033[92m Sessão removida com Sucesso! \033[0m");
        } catch (SessaoNaoEncontradaException e) {
            System.err.println(e.getMessage());
        }
    }
    private void atualizarSessao(){
        System.out.println("(Digite 0 a qualquer momento para sair)");
        String ID, horario,dia, idFilme;

        ID = lerDado("ID da Sessão");
        if (ID == null) return;
        try{
            fachada.procurarSessao(ID);
        } catch (SessaoNaoEncontradaException e) {
            System.err.println(e.getMessage());
            return;
        }
        System.out.println("Novo horario:");
        horario = lerHorario();
        if (horario == null) return;
        System.out.println("Novo dia:");
        dia= lerData();
        if (dia == null)return;
        idFilme = lerDado("ID do filme que será colocado nesse horário");
        if (idFilme==null)return;
        try {
            fachada.procurarFilmePorID(idFilme);
        } catch (FilmeNaoEstaCadastradoException e) {
            System.err.println(e.getMessage());
            return;
        }
        try {
            fachada.atualizarSessao(ID,horario,dia,idFilme);
            System.out.println("\033[92m Sessão atualizada com Sucesso! \033[0m");
        } catch (SessaoNaoEncontradaException | FilmeNaoEstaCadastradoException | SalaNaoEncontradaException | ConflitoHorarioException e) {
            System.err.println(e.getMessage());
        }
    }
    private void buscarSessaoporTitulo(){
        System.out.println("(Digite 0 a qualquer momento para sair)");
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
        System.out.println("(Digite 0 a qualquer momento para sair)");
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
    private void reservarSessaoInteira(){
        System.out.println("(Digite 0 a qualquer momento para sair)");
        String idSessao = lerDado("ID da sessão (preferencialmente criar uma nova antes)");
        if (idSessao == null) return;
        try {
            fachada.procurarSessao(idSessao);
            System.out.println("Quantidade de pessoas que irão participar do evento: ");
            int quantidadePessoas= scanner.nextInt();
            scanner.nextLine();
            fachada.reservarSessaoParaEntidade(idSessao, quantidadePessoas);
            System.out.println("\033[92m Sessão reservada com Sucesso! \033[0m");
        } catch (SessaoNaoEncontradaException | AssentoIndisponivelException  | SessaoIndisponivelParaReservaException e){
            System.err.println(e.getMessage());
        }
    }


}

