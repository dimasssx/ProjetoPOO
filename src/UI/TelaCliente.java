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
    private Cliente cliente;
    private FachadaCliente clienteFachada;
    private Scanner scanner;

    public TelaCliente(FachadaCliente clienteFachada, Cliente cliente) {
        this.cliente = cliente;
        this.clienteFachada = clienteFachada;
        scanner = new Scanner(System.in);
        hoje = MonthDay.now();
    }

    public void iniciar() {

        System.out.println("Bem-vindo ao MovieTime, " + cliente.getNome() + "!");
        System.out.println("---------------------------");
        exibicaoSessoesDeHoje(); //exibe somente os filmes e sessoes de hoje

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
                /*
                case "1":
                    TelaComprarIngresso comprar = new TelaComprarIngresso(fachada);
                    try {
                        comprar.iniciar();
                    } catch (AssentoIndisponivelException | SessaoNaoEncontradaException e) {
                        System.err.println(e);
                    }
                    break;
                 */
                case "2":
                    try {
                        buscarporDia();
                    } catch (NenhumaSessaoEncontradaException e) {
                        System.err.println(e);
                    }
                    break;
                case "3":
                    try {
                        buscarPorFilme();
                    } catch (NenhumFilmeEncontradoException e) {
                        System.err.println(e);
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

    private String extrairTituloDoToString(String filmeFormatado) {
        String[] partes = filmeFormatado.split("\\|");
        if (partes.length > 0) {
            return partes[0].replace("Título:", "").trim();
        }
        return "";
    }

    public void exibicaoSessoesDeHoje() {
        System.out.println("Filmes em Cartaz Hoje: " + hoje.format(DateTimeFormatter.ofPattern("dd/MM")));
        System.out.println("-------------------");
        ArrayList<String> filmesFormatados;

        try {
            filmesFormatados = clienteFachada.verCatalogo();
        } catch (NenhumFilmeEncontradoException e) {
            System.out.println("Nenhum filme encontrado.");
            return;
        }

        for (String filmeFormatado : filmesFormatados) {
            String titulo = extrairTituloDoToString(filmeFormatado);
            ArrayList<String> sessoesFormatadas;
            try {
                sessoesFormatadas = clienteFachada.acessarSessoesFormatadasPorFilmeEDia(titulo, LocalDate.from(hoje));
                if (!sessoesFormatadas.isEmpty()) {
                    System.out.println("Filme: " + filmeFormatado);
                    System.out.println("Sessões disponíveis:");
                    for (String sessao : sessoesFormatadas) {
                        System.out.println(sessao);
                    }
                }
            } catch (SessaoNaoEncontradaException e) {
                e.getMessage();
            }
        }
    }

    public void buscarporDia() {
        System.out.println("Digite a Data: (dd-MM)");
        String datainput = scanner.nextLine();

        try {
            MonthDay diaEscolhido = MonthDay.parse(datainput, DateTimeFormatter.ofPattern("dd-MM"));
            System.out.println("Sessões para o dia: " + diaEscolhido.format(DateTimeFormatter.ofPattern("dd-MM")));

            ArrayList<String> sessoesFormatadas = clienteFachada.acessarSessoesFormatadasPorDia(diaEscolhido);

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


    public void buscarPorFilme() {
        System.out.println("Digite o nome do Filme:");
        String tituloInput = scanner.nextLine().trim();

        ArrayList<String> filmesFormatados;
        try {
            filmesFormatados = clienteFachada.verCatalogo();
        } catch (NenhumFilmeEncontradoException e) {
            System.out.println("Nenhum filme encontrado no catálogo.");
            return;
        }

        boolean encontrouFilme = false;

        for (String filmeFormatado : filmesFormatados) {
            String tituloExtraido = extrairTituloDoToString(filmeFormatado);
            if (tituloExtraido.equalsIgnoreCase(tituloInput)) {
                encontrouFilme = true;
                try {
                    ArrayList<String> sessoesFormatadas = clienteFachada.acessarSessoesFormatadasPorFilme(tituloExtraido);
                    if (sessoesFormatadas.isEmpty()) {
                        System.out.println("Nenhuma sessão encontrada para o filme: " + tituloExtraido);
                    } else {
                        System.out.println("Sessões para o filme: " + tituloExtraido);
                        for (String sessao : sessoesFormatadas) {
                            System.out.println(sessao);
                            System.out.println("----------------------------");
                        }
                    }
                } catch (SessaoNaoEncontradaException e) {
                    System.out.println("Nenhuma sessão encontrada para o filme: " + tituloExtraido);
                }
                break;
            }
        }

        if (!encontrouFilme) {
            System.out.println("Filme não encontrado no catálogo.");
        }
    }
}