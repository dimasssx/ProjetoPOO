
package UI;

//import fachada.Cinema;
import fachada.FachadaCliente;
import fachada.Movietime;
import negocio.exceptions.*;
import negocio.SessoesNegocio;
import negocio.entidades.Cliente;
import negocio.entidades.*;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class TelaCliente {
    private MonthDay hoje;
    private FachadaCliente clienteFachada;
    private Scanner scanner;
    private Cliente cliente;
    private Movietime fachadaPrincipal;

    public TelaCliente(FachadaCliente clienteFachada,Cliente cliente,Movietime fachadaPrincipal) {
        this.clienteFachada = clienteFachada;
        scanner = new Scanner(System.in);
        hoje = MonthDay.now();
        this.cliente = cliente;
        this.fachadaPrincipal = fachadaPrincipal;
    }

    public void iniciar() {

        System.out.println("Bem-vindo ao MovieTime!");
        System.out.println("---------------------------");
        exibicaoSessoesDeHoje(); //exibe os filmes em cartaz e as sessões de hoje

        while(true){
            System.out.println("1 - Comprar ingresso");
            System.out.println("2 - Buscar sessões por dia");
            System.out.println("3 - Buscar sessoes por filme");
            System.out.println("4 - Gerenciamento de conta");
            System.out.println("5 - Logout");

            String opcao;

            try {
                opcao = scanner.nextLine();
            } catch (Exception e) {
                System.err.println("Digite um número");
                scanner.nextLine();
                continue;
            }

            switch (opcao){
                case "1":
                    TelaComprarIngresso comprar = new TelaComprarIngresso(clienteFachada,cliente);
                    comprar.iniciar();
                    break;

                case "2":
                    try {
                        buscarporDia();
                    } catch (SessaoNaoEncontradaException e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case "3":
                    try {
                        buscarPorFilme();
                    }
                    catch (SessaoNaoEncontradaException e){
                        System.err.println(e.getMessage());
                    }
                    break;
                case "4":
                    TelaGerenciamentoDeContaCliente telaGerenciamentoDeContaCliente = new TelaGerenciamentoDeContaCliente(fachadaPrincipal,cliente);
                    telaGerenciamentoDeContaCliente.iniciar();
                    break;
                case "5":
                    System.out.println("Saindo...");
                    return;
                default:
                    System.err.println("Opcao invalida");
            }
        }
    }

    public void exibicaoSessoesDeHoje() {
        System.out.println("Filmes em Cartaz Hoje: " + hoje.format(DateTimeFormatter.ofPattern("dd/MM")));
        System.out.println("-------------------");

        try {
            ArrayList<String> sessoesFormatadas = clienteFachada.procurarSessoesHoje();

            if (sessoesFormatadas.isEmpty()) {
                System.out.println("Nenhuma sessão disponível para hoje.");
            } else {
                for (String sessao : sessoesFormatadas) {
                    System.out.println(sessao);
                }
            }
        } catch (SessaoNaoEncontradaException e) {
            System.out.println("Nenhuma sessão encontrada para hoje.");
        }
    }

    public void buscarporDia() throws SessaoNaoEncontradaException {
        System.out.println("(digite 0 a qualquer momento para sair)");
        String dataInput = lerData();
        if (dataInput == null) return;

        try {
            ArrayList<String> sessoesFormatadas = clienteFachada.procurarSessaoporDia(dataInput);

            if (sessoesFormatadas.isEmpty()) {
                System.out.println("Nenhuma sessão encontrada para esta data.");
            } else {
                System.out.println("Sessões para o dia " + dataInput + ":");
                for (String sessao : sessoesFormatadas) {
                    System.out.println(sessao);
                    System.out.println("----------------------------");
                }
            }

        } catch ( SessaoNaoEncontradaException e) {
            System.out.println(e.getMessage());
        }
    }


    public void buscarPorFilme() throws SessaoNaoEncontradaException {
        System.out.println("(digite 0 a qualquer momento para sair)");
        String tituloInput = lerTituloFilme();
        if (tituloInput == null) return;

        try {
            clienteFachada.consultarFilme(tituloInput); // validação do catálogo

            ArrayList<String> sessoesFormatadas = clienteFachada.procurarSessaoPorTituloDoFilme(tituloInput);

            if (sessoesFormatadas.isEmpty()) {
                System.out.println("Nenhuma sessão encontrada para o filme: " + tituloInput);
            } else {
                System.out.println("Sessões para o filme: " + tituloInput);
                for (String sessao : sessoesFormatadas) {
                    System.out.println(sessao);
                    System.out.println("----------------------------");
                }
            }
        } catch (FilmeNaoEstaCadastradoException e) {
            System.err.println(e.getMessage());
        } catch (SessaoNaoEncontradaException e) {
            System.err.println(e.getMessage());
        }
    }

    private String lerData() {
        String data;
        do {
            System.out.println("Digite a data da sessão (dd-MM):");
            data = scanner.nextLine().trim();

            if (data.equals("0")) {
                System.out.println("Operação cancelada.");
                return null;
            }

            if (!isDataValida(data)) {
                System.err.println("Formato inválido! Use dd-MM (ex: 17-04).");
            }
        } while (!isDataValida(data));

        return data;
    }

    private boolean isDataValida(String data) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM");
            MonthDay.parse(data, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String lerTituloFilme() {
        String titulo;
        do {
            System.out.println("Digite o nome do Filme:");
            titulo = scanner.nextLine().trim();

            if (titulo.equals("0")) {
                System.out.println("Operação cancelada.");
                return null;
            }

            if (titulo.isEmpty()) {
                System.err.println("O título não pode estar vazio!");
            }

        } while (titulo.isEmpty());

        return titulo;
    }


}