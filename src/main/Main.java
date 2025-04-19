package main;

import UI.TelaLogin;
import fachada.Movietime;

public class Main{
    public static void main(String[] args) {
        TelaLogin telaLogin = new TelaLogin(new Movietime());
        telaLogin.iniciar();
    }
}
