package UI;

import fachada.FachadaCliente;

import java.util.*;

import negocio.entidades.*;
import negocio.exceptions.sessoes.SessaoJaExibidaException;
import negocio.exceptions.assentos.AssentoIndisponivelException;
import negocio.exceptions.ingressos.QuantidadeInvalidaException;
import negocio.exceptions.sessoes.SessaoNaoEncontradaException;
import static UI.Utils.ValidacaoEntradas.*;

public class TelaComprarIngresso {
    private final FachadaCliente fachada;
    private final Scanner scanner;
    private final Cliente cliente;

    // cores ANSI para melhorar a interface, vao estar presentes nos prints da tela para mudar a cor
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BOLD = "\u001B[1m";

    public TelaComprarIngresso(FachadaCliente fachada, Cliente cliente) {
        this.fachada = fachada;
        this.scanner = new Scanner(System.in);
        this.cliente = cliente;
    }

    public void iniciar() {
        imprimirCabecalho();
        
        String ID;
        System.out.println(ANSI_YELLOW + "Escolha a sessão pelo ID, caso não saiba, volte e busque a sessão que deseja por filme ou dia!" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "Digite 0 a qualquer momento para sair." + ANSI_RESET);
        
        while(true){
            ID = lerDado("ID da Sessão");
            if (ID == null) return;
            
            try {
                Sessao sessao = fachada.procurarSessao(ID);
                try{
                    fachada.verificarSessaoAindaValida(sessao);
                } catch (SessaoJaExibidaException e) {
                    System.err.println(e.getMessage());
                    return;
                }

                // Solicitar quantidade de ingressos
                int quantidadeIngressos;
                try {
                    quantidadeIngressos = solicitarQuantidadeIngressos();
                    if (quantidadeIngressos <= 0) {
                        continue;
                    }
                } catch (QuantidadeInvalidaException e) {
                    System.err.println( "► " + e.getMessage());
                    continue;
                }

                    TelaEscolhadeAssentos telaEscolhadeAssentos = new TelaEscolhadeAssentos(fachada, sessao);
                    ArrayList<Ingresso> ingressosSelecionados = telaEscolhadeAssentos.iniciar(quantidadeIngressos);

                    if (ingressosSelecionados == null || ingressosSelecionados.isEmpty()) {
                        System.out.println(ANSI_YELLOW + "Seleção de assentos cancelada." + ANSI_RESET);
                        continue;
                    }

                    if (ingressosSelecionados.size() != quantidadeIngressos) {
                        System.out.println(ANSI_RED + "Erro: Número de assentos selecionados (" + ingressosSelecionados.size() +
                                ") diferente do número de ingressos solicitados (" + quantidadeIngressos + ")." + ANSI_RESET);
                        continue;
                    }

                    // Verificar se existem assentos duplicados
                    boolean temDuplicados = verificarAssentosDuplicados(ingressosSelecionados);
                    if (temDuplicados) {
                        System.out.println(ANSI_RED + "Erro: Assentos duplicados detectados. Por favor, inicie a compra novamente." + ANSI_RESET);
                        continue;
                    }

                    String lanche = lerDado("Deseja comprar lanches? (S/N)");
                    if (lanche == null) return;
                    ArrayList<Lanche> lanches = new ArrayList<>();
                        if(lanche.equalsIgnoreCase("S")){
                            TelaEscolhaDeLanches telacompradelanches = new TelaEscolhaDeLanches(fachada);
                            lanches = telacompradelanches.iniciar();
                        }

                    boolean pagamentoRealizado = processarPagamento(sessao, ingressosSelecionados, quantidadeIngressos,lanches);
                    if (pagamentoRealizado) {
                        finalizarCompra(sessao, ingressosSelecionados);
                        return;
                    } else {
                        System.out.println(ANSI_YELLOW + "A compra foi cancelada." + ANSI_RESET);
                        return;
                    }
                }


             catch (SessaoNaoEncontradaException e) {
                System.err.println( "► " + e.getMessage());
            }
        }
    }
    
    private void imprimirCabecalho() {
        System.out.println("\n" + ANSI_BOLD);
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║                COMPRA DE INGRESSOS                ║");
        System.out.println("╚═══════════════════════════════════════════════════╝" + ANSI_RESET);
    }

    private boolean verificarAssentosDuplicados(ArrayList<Ingresso> ingressos) {
        ArrayList<String> assentosVerificados = new ArrayList<>();
        
        for (Ingresso ingresso : ingressos) {
            Assento assento = ingresso.getAssento();
            String posicaoAssento = (char)('A' + (assento.getFileira()-1)) + "" + assento.getPoltrona();
            
            if (assentosVerificados.contains(posicaoAssento)) {
                return true; // Encontrou um assento duplicado
            }
            
            assentosVerificados.add(posicaoAssento);
        }
        
        return false; // Não há duplicados
    }
    
    private int solicitarQuantidadeIngressos() throws QuantidadeInvalidaException {
        System.out.println("\n" + ANSI_BOLD + "═════════════════════════════════════════" + ANSI_RESET);
        System.out.println(ANSI_BOLD + "  QUANTIDADE DE INGRESSOS" + ANSI_RESET);
        System.out.println(ANSI_BOLD + "═════════════════════════════════════════" + ANSI_RESET);
        
        System.out.println("Digite a quantidade de ingressos que serão comprados:");
        System.out.print("► ");
        
        while(true) {
            try {
                int quantidade = scanner.nextInt();
                scanner.nextLine();
                
                if (quantidade <= 0) {
                    throw new QuantidadeInvalidaException();
                }
                return quantidade;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                throw new QuantidadeInvalidaException("Digite um número válido para a quantidade de ingressos.");
            }
        }
    }
    
    private boolean processarPagamento(Sessao sessao, ArrayList<Ingresso> ingressos, int quantidadeIngressos, ArrayList<Lanche> lanches) {
        double valorTotal = fachada.calcularValorCompra(cliente, sessao, ingressos,lanches);

        imprimirResumoDaCompra(sessao, ingressos, quantidadeIngressos, valorTotal,lanches);
        
        String resultadoPagamento;
        
        while (true) {
            System.out.println("\n" + ANSI_BOLD + "═════════════════════════════════════════" + ANSI_RESET);
            System.out.println(ANSI_BOLD + "  ESCOLHA A FORMA DE PAGAMENTO" + ANSI_RESET);
            System.out.println(ANSI_BOLD + "═════════════════════════════════════════" + ANSI_RESET);
            
            System.out.println("1 - Cartão de Crédito");
            System.out.println("2 - Cartão de Débito");
            System.out.println("3 - PIX");
            System.out.println("0 - Cancelar Compra e Voltar à Tela Principal");
            System.out.print("► ");
            
            String opcao = scanner.nextLine();
            
            switch (opcao) {
                case "1":
                    String numeroCartaoCredito = null;
                    while (true) {
                        numeroCartaoCredito = lerDado("Número do cartao(apenas números)");
                        if (numeroCartaoCredito == null) {
                            break;
                        }

                        // Verificar se o cartão tem exatamente 16 dígitos e apenas números
                        if (!numeroCartaoCredito.matches("\\d{16}")) {
                            System.out.println(ANSI_RED + "Número de cartão inválido. O cartão deve ter exatamente 16 dígitos numéricos." + ANSI_RESET);
                            continue;
                        }
                        break;
                    }
                    
                    if (numeroCartaoCredito == null) {
                        continue;
                    }
                    
                    String titularCartaoCredito = lerDado("Titular do cartao");
                    if (titularCartaoCredito == null) {
                        continue;
                    }
                    resultadoPagamento = fachada.pagarComCredito(numeroCartaoCredito, titularCartaoCredito, valorTotal);
                    break;

                case "2":
                    String numeroCartaoDebito = null;
                    while (true) {
                        numeroCartaoDebito = lerDado("Número do cartao(apenas números)");
                        if (numeroCartaoDebito == null) {
                            break;
                        }
                        // Verificar se o cartão tem exatamente 16 dígitos e apenas números
                        if (!numeroCartaoDebito.matches("\\d{16}")) {
                            System.out.println(ANSI_RED + "Número de cartão inválido. O cartão deve ter exatamente 16 dígitos numéricos." + ANSI_RESET);
                            continue;
                        }
                        break;
                    }
                    if (numeroCartaoDebito == null) {
                        continue; 
                    }
                    String titularCartaoDebito = lerDado("Titular do cartao");            
                    if (titularCartaoDebito == null) {
                        continue; 
                    }
                    resultadoPagamento = fachada.pagarComDebito(numeroCartaoDebito, titularCartaoDebito, valorTotal);
                    break;

                case "3":
                    resultadoPagamento = fachada.pagarComPIX(valorTotal);
                    break;

                case "0":
                    System.out.println(ANSI_YELLOW + "Compra cancelada pelo usuário. Retornando à tela principal..." + ANSI_RESET);
                    return false;

                default:
                    System.out.println(ANSI_RED + "Método de pagamento inválido. Por favor, escolha uma opção válida." + ANSI_RESET);
                    continue;
            }
            
            break;
        }
        
        // Exibir resultado do pagamento
        System.out.println(resultadoPagamento);
        System.out.println(ANSI_GREEN + "\n✓ Pagamento realizado com sucesso!" + ANSI_RESET);
        return true;
    }
    
    private void imprimirResumoDaCompra(Sessao sessao, ArrayList<Ingresso> ingressos, int quantidadeIngressos, double valorTotal,ArrayList<Lanche> lanches) {
        System.out.println("\n" + ANSI_BOLD + "═════════════════════════════════════════" + ANSI_RESET);
        System.out.println(ANSI_BOLD + "  RESUMO DA COMPRA" + ANSI_RESET);
        System.out.println(ANSI_BOLD + "═════════════════════════════════════════" + ANSI_RESET);
        
        System.out.println(ANSI_BOLD + "Filme: " + ANSI_RESET + sessao.getFilme().getTitulo());
        System.out.println(ANSI_BOLD + "Sessão: " + ANSI_RESET + sessao.getHorario() + " - " + sessao.getDiaFormatado());
        System.out.println(ANSI_BOLD + "Sala: " + ANSI_RESET + sessao.getSala().getCodigo() + " (" + sessao.getSala().getTipo() + ")");
        System.out.println(ANSI_BOLD + "Quantidade de ingressos: " + ANSI_RESET + quantidadeIngressos);
        if(lanches.isEmpty()){
            System.out.println(ANSI_BOLD + "Nenhum lanche escolhido " + ANSI_RESET + quantidadeIngressos);
        }else{
            System.out.println(ANSI_BOLD + "Lanches comprados: " + ANSI_RESET + lanches.size());
        }
        // contar ingressos de cada tipo

        int ingressosMeia = 0;
        int ingressosInteira = 0;
        for (Ingresso ingresso : ingressos) {
            if (ingresso.getTipo().equals("Meia")) {
                ingressosMeia++;
            } else {
                ingressosInteira++;
            }
        }
        double valorLanches = 0.0;
        for(Lanche l : lanches){
            valorLanches+= l.getPreco();
        }
        double valorUnitario = sessao.getValorIngresso();
        System.out.println(ANSI_BOLD + "Valor unitário da Sessão: " + ANSI_RESET + "R$ " + String.format("%.2f", valorUnitario));

        if (ingressosInteira > 0) {
            System.out.println(ANSI_BOLD + "Ingressos Inteira: " + ANSI_RESET + ingressosInteira + 
                               " x R$ " + String.format("%.2f", valorUnitario) + 
                               " = R$ " + String.format("%.2f", ingressosInteira * valorUnitario));
        }
        if (ingressosMeia > 0) {
            System.out.println(ANSI_BOLD + "Ingressos Meia: " + ANSI_RESET + ingressosMeia + 
                               " x R$ " + String.format("%.2f", valorUnitario * 0.5) + 
                               " = R$ " + String.format("%.2f", ingressosMeia * valorUnitario * 0.5));
        }

        if(lanches.size()> 0){
            System.out.println(ANSI_BOLD + "Total em Lanches: " + ANSI_RESET + "R$ "+valorLanches);
            Map<String, Integer> contador = new HashMap<>();
            Map<String, Double> precos = new HashMap<>();
            for (Lanche l : lanches) {
                contador.put(l.getNome(), contador.getOrDefault(l.getNome(), 0) + 1);
                precos.put(l.getNome(), l.getPreco());
            }
            for (String nome : contador.keySet()) {
                int qtd = contador.get(nome);
                double precoUnit = precos.get(nome);
                System.out.println(ANSI_BOLD + nome + ": x" + qtd + ANSI_RESET + " R$ " + String.format(Locale.FRANCE,"%.2f",precoUnit * qtd));
            }
        }

        // mostrar desconto para o VIP
        if (cliente instanceof ClienteVIP) {
            double subtotal = (ingressosInteira * valorUnitario) + (ingressosMeia * valorUnitario * 0.5)+(valorLanches);
            double descontoVIP = subtotal * 0.35;
            System.out.println(ANSI_BOLD + "Subtotal: " + ANSI_RESET + "R$ " + String.format("%.2f", subtotal));
            System.out.println(ANSI_BOLD + "Desconto VIP (35%): " + ANSI_RESET + "R$ " + String.format("%.2f", descontoVIP));
        }
        
        System.out.println(ANSI_BOLD + "Valor total: " + ANSI_RESET + "R$ " + String.format("%.2f", valorTotal));

        System.out.println("\n" + ANSI_BOLD + "Assentos selecionados:" + ANSI_RESET);
        for (int i = 0; i < ingressos.size(); i++) {
            Ingresso ingresso = ingressos.get(i);
            Assento assento = ingresso.getAssento();
            System.out.println("  " + (i+1) + ". " + (char)('A' + (assento.getFileira()-1)) + assento.getPoltrona() + 
                               " - Tipo: " + ingresso.getTipo());
        }
    }
    
    private void finalizarCompra(Sessao sessao, ArrayList<Ingresso> ingressos) {
        try {
            for (Ingresso ingresso : ingressos) {
                Assento assento = ingresso.getAssento();
                fachada.getSessoesNegocio().reservarAssento(sessao, assento.getFileira() - 1, assento.getPoltrona() - 1 );
            }

            fachada.adicionarIngresosAConta(cliente, ingressos);

            try {
                Cliente clienteAtualizado = fachada.getClienteNegocio().buscarCliente(cliente.getNomeDeUsuario(), cliente.getSenha());
                int totalIngressos = clienteAtualizado.getIngressosComprados().size();
                
                System.out.println("\n" + ANSI_GREEN + ANSI_BOLD);
                System.out.println("╔═══════════════════════════════════════════════════╗");
                System.out.println("║          COMPRA FINALIZADA COM SUCESSO!           ║");
                System.out.println("╚═══════════════════════════════════════════════════╝" + ANSI_RESET);
                System.out.println(ANSI_YELLOW + "Os seguintes ingressos foram adicionados à sua conta, apresente-os no cinema e apreveite o filme!." + ANSI_RESET);
                for(Ingresso i : ingressos){
                    System.out.println(i);
                }
                System.out.println("Total de ingressos na sua conta: " + totalIngressos);
            } catch (Exception e) {
                System.err.println(ANSI_RED + "Erro ao verificar cliente atualizado: " + e.getMessage() + ANSI_RESET);
            }
            
        } catch (AssentoIndisponivelException | SessaoNaoEncontradaException e) {
            System.err.println(ANSI_RED + "Erro ao finalizar a compra: " + e.getMessage() + ANSI_RESET);
        }
    }
}

