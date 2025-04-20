package UI;

import fachada.FachadaCliente;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import negocio.entidades.Assento;
import negocio.entidades.Cliente;
import negocio.entidades.ClienteVIP;
import negocio.entidades.Ingresso;
import negocio.entidades.Sessao;
import negocio.exceptions.assentos.AssentoIndisponivelException;
import negocio.exceptions.ingressos.QuantidadeInvalidaException;
import negocio.exceptions.sessoes.SessaoNaoEncontradaException;

public class TelaComprarIngresso {
    private FachadaCliente fachada;
    private Scanner scanner;
    private Cliente cliente;

    public TelaComprarIngresso(FachadaCliente fachada, Cliente cliente) {
        this.fachada = fachada;
        this.scanner = new Scanner(System.in);
        this.cliente = cliente;
    }

    public void iniciar() {
        String ID;

        System.out.println("Escolha a sessão pelo ID, caso não saiba, volte e busque a sessão que deseja por filme ou dia! (digite 0 a qualquer momento para sair)");
        while(true){
            ID = lerDado("ID da Sessão");
            if (ID == null) return;
            
            try {
                Sessao sessao = fachada.procurarSessao(ID);
                
                // Solicitar quantidade de ingressos
                int quantidadeIngressos;
                try {
                    quantidadeIngressos = solicitarQuantidadeIngressos();
                    if (quantidadeIngressos <= 0) {
                        continue; // Volta para a seleção de sessão
                    }
                } catch (QuantidadeInvalidaException e) {
                    System.err.println(e.getMessage());
                    continue;
                }
                
                // Processo de escolha de assentos
                TelaEscolhadeAssentos telaEscolhadeAssentos = new TelaEscolhadeAssentos(fachada, sessao);
                ArrayList<Ingresso> ingressosSelecionados = telaEscolhadeAssentos.iniciar(quantidadeIngressos);
                
                if (ingressosSelecionados == null || ingressosSelecionados.isEmpty()) {
                    System.out.println("Seleção de assentos cancelada.");
                    continue; // Volta para a seleção de sessão
                }
                
                // Verificar se o número de ingressos selecionados corresponde ao solicitado
                if (ingressosSelecionados.size() != quantidadeIngressos) {
                    System.err.println("Erro: Número de assentos selecionados (" + ingressosSelecionados.size() + 
                                      ") diferente do número de ingressos solicitados (" + quantidadeIngressos + ").");
                    continue; // Volta para a seleção de sessão
                }
                
                // Verificar se existem assentos duplicados
                boolean temDuplicados = verificarAssentosDuplicados(ingressosSelecionados);
                if (temDuplicados) {
                    System.err.println("Erro: Assentos duplicados detectados. Por favor, inicie a compra novamente.");
                    continue; // Volta para a seleção de sessão
                }
                
                // Processar pagamento
                boolean pagamentoRealizado = processarPagamento(sessao, ingressosSelecionados, quantidadeIngressos);
                if (pagamentoRealizado) {
                    // Finaliza a compra
                    finalizarCompra(sessao, ingressosSelecionados);
                    return; // Encerra o processo de compra após o sucesso
                } else {
                    System.out.println("A compra foi cancelada.");
                    continue; // Volta para escolher uma nova sessão
                }
                
            } catch (SessaoNaoEncontradaException e) {
                System.err.println(e.getMessage());
            }
        }
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
        System.out.println("Digite a quantidade de Ingressos que serão comprados:");
        while (true) {
            try {
                int quantidade = scanner.nextInt();
                scanner.nextLine(); // Consumir quebra de linha
                
                if (quantidade <= 0) {
                    throw new QuantidadeInvalidaException();
                }
                return quantidade;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Limpar o buffer
                throw new QuantidadeInvalidaException("Digite um número válido para a quantidade de ingressos.");
            }
        }
    }
    
    private boolean processarPagamento(Sessao sessao, ArrayList<Ingresso> ingressos, int quantidadeIngressos) {
        // Calcular valor total
        double valorTotal = fachada.getClienteNegocio().calcularValorTotal(cliente, sessao, quantidadeIngressos);
        
        System.out.println("\n=== Resumo da Compra ===");
        System.out.println("Filme: " + sessao.getFilme().getTitulo());
        System.out.println("Sessão: " + sessao.getHorario() + " - " + sessao.getDiaFormatado());
        System.out.println("Sala: " + sessao.getSala().getCodigo() + " (" + sessao.getSala().getTipo() + ")");
        System.out.println("Quantidade de ingressos: " + quantidadeIngressos);
        System.out.println("Valor unitário: R$ " + String.format("%.2f", sessao.getValorIngresso()));
        
        // Mostrar desconto para VIP
        if (cliente instanceof ClienteVIP) {
            System.out.println("Desconto VIP: 35%");
        }
        
        System.out.println("Valor total: R$ " + String.format("%.2f", valorTotal));
        
        // Listar assentos selecionados
        System.out.println("\nAssentos selecionados:");
        for (int i = 0; i < ingressos.size(); i++) {
            Assento assento = ingressos.get(i).getAssento();
            System.out.println("  " + (i+1) + ". " + (char)('A' + (assento.getFileira()-1)) + assento.getPoltrona());
        }
        
        while (true) {
            System.out.println("\nEscolha a forma de pagamento:");
            System.out.println("1 - Cartão de Crédito");
            System.out.println("2 - Cartão de Débito");
            System.out.println("3 - PIX");
            System.out.println("0 - Cancelar Compra");
            
            String opcao = scanner.nextLine();
            
            switch (opcao) {
                case "1":
                    String numeroCartaoCredito = lerDado("Número do cartao(apenas digitos)");
                    String titularCartaoCredito = lerDado("Titular do cartao");
                    System.out.println(fachada.pagarComCredito(numeroCartaoCredito,titularCartaoCredito,valorTotal));
                    return true;
                case "2":
                    String numeroCartaoDebito = lerDado("Número do cartao(apenas digitos)");
                    String titularCartaoDebito = lerDado("Titular do cartao");
                    System.out.println(fachada.pagarComDebito(numeroCartaoDebito,titularCartaoDebito,valorTotal));
                    return true;
                case "3":
                    System.out.println(fachada.pagarComPIX(valorTotal));
                    return true;
                case "0":
                    System.out.println("Compra cancelada pelo usuário.");
                    return false;
                default:
                    System.err.println("Método de pagamento inválido. Por favor, escolha uma opção válida.");
            }
        }
    }
    
    private void finalizarCompra(Sessao sessao, ArrayList<Ingresso> ingressos) {
        try {
            for (Ingresso ingresso : ingressos) {
                Assento assento = ingresso.getAssento();
                fachada.getSessoesNegocio().reservarAssento(
                    sessao, 
                    assento.getFileira() - 1, // Ajustar índice
                    assento.getPoltrona() - 1 // Ajustar índice
                );
            }

            fachada.getClienteNegocio().adicionarIngressosAoCliente(cliente, ingressos);

            try {
                Cliente clienteAtualizado = fachada.getClienteNegocio().buscarCliente(cliente.getNomeDeUsuario(), cliente.getSenha());
                int totalIngressos = clienteAtualizado.getIngressosComprados().size();
                
                System.out.println("\n\033[92m Compra finalizada com sucesso! \033[0m");
                System.out.println("Seus ingressos foram adicionados à sua conta.");
                System.out.println("Total de ingressos na sua conta: " + totalIngressos);
            } catch (Exception e) {
                System.err.println("Erro ao verificar cliente atualizado: " + e.getMessage());
            }
            
        } catch (AssentoIndisponivelException | SessaoNaoEncontradaException e) {
            System.err.println("Erro ao finalizar a compra: " + e.getMessage());
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
                System.err.println(campo + " não pode estar vazio!");
                continue;
            }
            return dado;
        }
    }
}
