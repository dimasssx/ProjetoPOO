package main;
import UI.TelaCliente;
import UI.TelaGerente;
import fachada.FachadaCliente;
import fachada.FachadaGerente;
import negocio.entidades.Cliente;
import negocio.entidades.ClientePadrao;

public class Main{
    public static void main(String[] args) {
        TelaGerente telaGerente = new TelaGerente(new FachadaGerente());
        telaGerente.iniciar();
    }
}
