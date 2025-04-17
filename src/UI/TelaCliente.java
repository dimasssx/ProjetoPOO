
package UI;

//import fachada.Cinema;
import fachada.FachadaCliente;
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
    MonthDay hoje;
    private FachadaCliente clienteFachada;
    private Scanner scanner;

    public TelaCliente(FachadaCliente clienteFachada) {
        this.clienteFachada = clienteFachada;
        scanner = new Scanner(System.in);
        hoje = MonthDay.now();
    }

    public void iniciar() {

        System.out.println("Bem-vindo ao MovieTime!");
        System.out.println("---------------------------");
        exibicaoSessoesDeHoje(); //exibe os filmes em cartaz e as sessões de hoje

        while(true){
            System.out.println("1 - Comprar ingresso");
            System.out.println("2 - Buscar sessões por dia");
            System.out.println("3 - Buscar sessoes por filme");
            System.out.println("4 - Logout");

            String opcao;

            try {
                opcao = scanner.nextLine();
            } catch (Exception e) {
                System.err.println("Digite um número");
                scanner.nextLine();
                continue;
            }

            switch (opcao){
//                case "1":
//                    TelaComprarIngresso comprar = new TelaComprarIngresso(clienteFachada);
//                    try {
//                        comprar.iniciar();
//                    } catch (AssentoIndisponivelException | SessaoNaoEncontradaException e) {
//                        System.err.println(e);
//                    }
//                    break;

                case "2":
                    try {
                        buscarporDia();
                    } catch (NenhumaSessaoEncontradaException e) {
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
                    //gerenciamento de conta
                case "5":
                    System.out.println("Saindo...");
                    return;
                default:
                    System.err.println("Opcao invalida");
            }
        }
    }

//    private String extrairTituloDoToString(String filmeFormatado) {
//        String[] partes = filmeFormatado.split("\\|");
//        if (partes.length > 0) {
//            return partes[0].replace("Título:", "").trim();
//        }
//        return "";
//    }

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
                    System.out.println("----------------------------");
                }
            }
        } catch (SessaoNaoEncontradaException e) {
            System.out.println("Nenhuma sessão encontrada para hoje.");
        }
    }

    public void buscarporDia() throws NenhumaSessaoEncontradaException {
        System.out.println("Digite a Data: (dd-MM)");
        String datainput = scanner.nextLine();

        try {
            ArrayList<String> sessoesFormatadas = clienteFachada.procurarSessaoporDia(datainput);

            if (sessoesFormatadas.isEmpty()) {
                System.out.println("Nenhuma sessão encontrada para esta data.");
            } else {
                for (String sessao : sessoesFormatadas) {
                    System.out.println(sessao);
                }
            }

        } catch (SessaoNaoEncontradaException e) {
            System.out.println("Nenhuma sessão encontrada para esta data.");
        } catch (Exception e) {
            System.err.println("Formato de data inválido. Use dd-MM.");
        }
    }


    public void buscarPorFilme() throws SessaoNaoEncontradaException {
        System.out.println("Digite o nome do Filme:");
        String tituloInput = scanner.nextLine().trim();

        try {
            clienteFachada.consultarFilme(tituloInput);

            ArrayList<String> sessoesFormatadas = clienteFachada.procurarSessaoPorTituloDoFilme(tituloInput);

            if (sessoesFormatadas.isEmpty()) {
                System.err.println("Nenhuma sessão encontrada para o filme: " + tituloInput);
            } else {
                System.out.println("Sessões para o filme: " + tituloInput);
                for (String sessao : sessoesFormatadas) {
                    System.out.println(sessao);
                    System.out.println("----------------------------");
                }
            }

        } catch (FilmeNaoEstaCadastradoException e) {
            System.err.println("Filme não encontrado no catálogo: " + tituloInput);
        } catch (SessaoNaoEncontradaException e) {
            System.err.println("Nenhuma sessão encontrada para o filme: " + tituloInput);
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar o filme.");
        }
    }


}