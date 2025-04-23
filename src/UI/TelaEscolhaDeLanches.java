package UI;

import fachada.FachadaCliente;
import negocio.entidades.Lanche;

import java.util.ArrayList;

import static UI.Utils.ValidacaoEntradas.lerDado;

public class TelaEscolhaDeLanches {
    private FachadaCliente fachada;


    public TelaEscolhaDeLanches(FachadaCliente fachadaCliente){
        this.fachada = fachadaCliente;
    }

    public ArrayList<Lanche> iniciar(){
        ArrayList<Lanche> escolhidos = new ArrayList<>();
        ArrayList<Lanche> cardapio = fachada.listarCardapio();
        String opcao;

        System.out.println("0 - Encerrar escolha de lanches");
        for (int i = 0; i < cardapio.size(); i++) {
            System.out.println((i + 1) + " - " + cardapio.get(i));
        }

        while(true){
            opcao = lerDado("Escolha uma opção");
            if (opcao == null) break;
            try{
                int indice = Integer.parseInt(opcao) - 1;
                if (indice >= 0 && indice < cardapio.size()) {
                    Lanche escolhido = cardapio.get(indice);
                    escolhidos.add(escolhido);
                    System.out.println("Adicionado: " + escolhido.getNome());
                } else {
                    System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Entrada inválida. Digite um número.");
                 }
             }
        return escolhidos;
    }
    public double calcularTotal(ArrayList<Lanche> lanches) {
        double total = 0.0;
        for (Lanche lanche : lanches) {
            total += lanche.getPreco();
        }
        return total;
    }
}
