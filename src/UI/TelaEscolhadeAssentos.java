package UI;

import fachada.FachadaCliente;
import negocio.entidades.Cliente;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TelaEscolhadeAssentos {
    FachadaCliente fachada;
    Cliente cliente;
    Scanner scanner;
    String sessao;
    public TelaEscolhadeAssentos(FachadaCliente fachada,Cliente cliente,String sessao){
        this.fachada = fachada;
        this.cliente = cliente;
        this.sessao = sessao;
        scanner = new Scanner(System.in);
    }

    public void iniciar(){
        System.out.println("Digite a quantidade de Ingressos que serão comprados");
        int ingressos;
        while (true) {
            try {
                ingressos = scanner.nextInt();
                scanner.nextLine();
                if (ingressos < 0) {
                    System.err.println("Digite um número maior que zero");
                    continue;
                } else if (ingressos == 0) {
                    return;
                }
                break;
            }catch (InputMismatchException e) {
                System.err.println("Digite um numero valido");
                scanner.nextLine();
            }
           fachada.visualizarAssentosDaSessao(sessao);

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
            }if(campo.equals("Ingressos") && Integer.parseInt(dado) <0){
                System.err.println("Quantidade de ingresso invalida");
                continue;
            }
            return dado;
        }

    }
}
