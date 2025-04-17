package main;

import UI.TelaGerente;
import fachada.FachadaGerente;

public class Main {
    public static void main(String[] args) {
        TelaGerente telaGerente = new TelaGerente(new FachadaGerente());
        telaGerente.iniciar();
    }
}
