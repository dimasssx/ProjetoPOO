package negocio;

import negocio.entidades.Lanche;

import java.util.ArrayList;

public class CardapioNegocio {

    public ArrayList<Lanche> obterCardapio() {
        ArrayList<Lanche> cardapio = new ArrayList<>();
        cardapio.add(new Lanche("Pipoca Média", 15.00));
        cardapio.add(new Lanche("Pipoca Grande", 25.00));
        cardapio.add(new Lanche("Refrigerante",  7.00));
        cardapio.add(new Lanche("Suco Natural",  9.00));
        cardapio.add(new Lanche("Combo Pipoca Média + Refri", 20.00));
        cardapio.add(new Lanche("Combo Pipoca Grande + Refri", 30.00));
        return cardapio;
    }
}
