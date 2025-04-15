package UI;

import fachada.Cinema;
import fachada.FachadaCliente;
import fachada.FachadaGerente;
import negocio.exceptions.AssentoIndisponivelException;
import negocio.exceptions.NenhumFilmeEncontradoException;
import negocio.exceptions.SessaoNaoEncontradaException;
import negocio.SessoesNegocio;
import negocio.entidades.Cliente;
import negocio.entidades.*;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class TelaCliente {
    MonthDay hoje;
    private FachadaCliente fachada;
    private Scanner scanner;

    public TelaCliente(FachadaCliente fachada, Cliente cliente) {
        this.fachada = fachada;
        scanner = new Scanner(System.in);
        hoje = MonthDay.now();
    }

    public void iniciar() {

        System.out.println("Bem-vindo ao MovieTime");
        System.out.println("---------------------------");
        exibirCatalogo(); //somente dos filmes de hoje

        while(true){
            System.out.println("1 - Comprar ingresso");
            System.out.println("2 - Buscar sessões por dia");
            System.out.println("3 - Buscar sessoes por filme");
            System.out.println("4 - Logout");

            int opcao = -1;

            try {
                opcao = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.err.println("Digite um número");
                scanner.nextLine();
                continue;
            }

            switch (opcao){
                case 1:
                    TelaComprarIngresso comprar = new TelaComprarIngresso(fachada);
                    try {
                        comprar.iniciar();
                    } catch (AssentoIndisponivelException | SessaoNaoEncontradaException e) {
                        System.err.println(e);
                    }
                    break;
                case 2:
                    try {
                        buscarporDia();
                    } catch (NenhumFilmeEncontradoException e) {
                        System.err.println(e);
                    }
                    break;
                case 3:
                    try {
                        buscarPorFilme();
                    } catch (NenhumFilmeEncontradoException e) {
                        System.err.println(e);
                    }
                    break;
                case 4:
                    System.out.println("Saindo...");
                    return;
                default:
                    System.err.println("Opcao invalida");
            }
        }
    }
    public void exibirCatalogo() {
        System.out.println("Filmes em Cartaz Hoje:" + hoje.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM")));
        System.out.println("-------------------");
        FachadaCliente fachadaCliente= new FachadaCliente();
        ArrayList<Filme> filmes = null;
        try {
            filmes = fachadaCliente.imprimirCatalogo();
        } catch (NenhumFilmeEncontradoException e) {
            e.getMessage();
        }

        for (Filme filme : filmes) {
            try {
                ArrayList<Sessao> sessoes = fachadaCliente.procurarSessaoTitulo(filme.getTitulo());
                if (sessoes.isEmpty()) {
                    continue;
                }
                System.out.println("Filme: " + filme);
                System.out.println("Sessoes disponiveis:");
                for (Sessao sessao : sessoes) {
                    if (sessao.getDia().equals(hoje)){
                        System.out.println(sessao.getSala().getCodigo() + " " + sessao.getSala().getTipo() + " " + sessao.getHorario() + " - " + sessao.getDiaFormatado());
                    }
                }
            } catch (SessaoNaoEncontradaException e) {
            }
        }
    }

    public void buscarporDia() throws NenhumFilmeEncontradoException {
        System.out.println("Digite a Data: (dd-MM)");
        String datainput = scanner.nextLine();

        try {
            MonthDay diaescolhido = MonthDay.parse(datainput, DateTimeFormatter.ofPattern("dd-MM"));
            System.out.println("Sessões para o dia: " + diaescolhido.format(DateTimeFormatter.ofPattern("dd-MM")));

            ArrayList<Filme> filmes = fachada.imprimirCatalogo();
            boolean encontrouSessao = false;

            for (Filme filme : filmes) {
                try {
                    ArrayList<Sessao> sessoes = fachada.procurarSessaoTitulo(filme.getTitulo());

                    for (Sessao s : sessoes) {
                        if (s.getDia().equals(diaescolhido)) {
                            System.out.println("Filme: " + filme.getTitulo());
                            System.out.println(s);
                            encontrouSessao = true;
                        }
                    }
                } catch (SessaoNaoEncontradaException e) {
                }
            }

            if (!encontrouSessao) {
                System.out.println("Nenhuma sessão encontrada para esta data.");
            }

        } catch (Exception e) {
            System.err.println("Formato de data inválido. Use dd-MM.");
        }
    }

    public void buscarPorFilme() throws NenhumFilmeEncontradoException {
        System.out.println("Digite o nome do Filme:");
        String tituloInput = scanner.nextLine().trim();

        ArrayList<Filme> filmes = fachada.imprimirCatalogo();
        boolean encontrouFilme = false;

        for (Filme filme : filmes) {
            if (filme.getTitulo().equalsIgnoreCase(tituloInput)) {
                encontrouFilme = true;
                try {
                    ArrayList<Sessao> sessoes = fachada.procurarSessaoTitulo(filme.getTitulo());

                    if (sessoes.isEmpty()) {
                        System.out.println("Nenhuma sessão encontrada para o filme: " + filme.getTitulo());
                    } else {
                        System.out.println("Sessões para o filme: " + filme.getTitulo());
                        for (Sessao s : sessoes) {
                            System.out.println("Dia: " + s.getDia().format(DateTimeFormatter.ofPattern("dd-MM")));
                            System.out.println("Horário: " + s.getHorario());
                            System.out.println(s);
                            System.out.println("----------------------------");
                        }
                    }

                } catch (SessaoNaoEncontradaException e) {
                    System.out.println("Nenhuma sessão encontrada para o filme: " + filme.getTitulo());
                }
                break;
            }
        }

        if (!encontrouFilme) {
            System.out.println("Filme não encontrado no catálogo.");
        }
    }
}