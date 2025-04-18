package main;
import UI.TelaCliente;
import UI.TelaGerente;
import UI.TelaLogin;
import fachada.FachadaCliente;
import fachada.FachadaGerente;
import fachada.Movietime;
import negocio.entidades.Cliente;
import negocio.entidades.ClientePadrao;

public class Main{
    public static void main(String[] args) {
        TelaLogin telaLogin = new TelaLogin(new Movietime());
        telaLogin.iniciar();
    }
}
