package UI;
import fachada.FachadaCliente;
import fachada.Movietime;
import negocio.entidades.Cliente;
import negocio.entidades.ClientePadrao;
import negocio.entidades.ClienteVIP;
import negocio.exceptions.usuario.ClienteJaExisteException;
import negocio.exceptions.usuario.ClienteNaoEncontradoException;

import java.util.Scanner;

public class TelaLogin {

    private Scanner scanner;
    private Movietime fachada;

    public TelaLogin(Movietime fachada) {
        this.scanner = new Scanner(System.in);
        this.fachada = fachada;
    }

    public void iniciar() {
        System.out.println("Bem-Vindo ao MovieTime!");
        System.out.println("---------------------------");

        while (true) {
            System.out.println("1 - Fazer Login");
            System.out.println("2 - Não possui uma conta? Cadastre-se");
            System.out.println("3 - Sair");

            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1":
                    try {
                        fazerLogin();
                    }catch (ClienteNaoEncontradoException e){
                        System.err.println("Login ou senha incorreto(s)\n");

                    }
                    break;
                case "2":
                    try {
                        cadastrarCliente();
                    } catch (ClienteJaExisteException e) {
                        System.err.println(e.getMessage());;
                    }
                    break;
                case "3":
                    System.out.println("Saindo...");
                    return;

                default:
                    System.err.println("Opção Invalida");
            }
        }
    }

    public void cadastrarCliente() throws ClienteJaExisteException{
        System.out.println("-------------");
        System.out.println("Cadastro (Digite 0 a qualquer momento para sair)");
        System.out.println("-------------");
        String login = lerDado("Usuário");
        if (login==null) return;
        String senha = lerDado("Senha");
        if (senha==null) return;
        String nome = lerDado("Nome");
        if (nome==null) return;

        fachada.cadastrarCliente(nome, login, senha);
        System.out.println("Cadastro realizado!");
    }

    public void fazerLogin() throws ClienteNaoEncontradoException {
        System.out.println("-------------");
        System.out.println("Login (Digite 0 a qualquer momento para sair)");
        System.out.println("-----------------------");
        String login = lerDado("Login");
        if (login ==null) return;
        String senha = lerDado("Senha");
        if (senha ==null) return;
        checarCredenciais(login, senha);
    }

    public void checarCredenciais(String login, String senha) throws ClienteNaoEncontradoException {
        if(login.equals("admin") && senha.equals("123")){
            TelaGerente telaGerente = new TelaGerente(fachada.getFachadaGerente());
            telaGerente.iniciar();
        }else{
            Cliente tipologin = fachada.autenticar(login,senha);
            if (tipologin instanceof ClientePadrao){
                FachadaCliente fachadaCliente = fachada.getFachadaCliente();
                TelaCliente telaCliente = new TelaCliente(fachadaCliente,tipologin,fachada);
                telaCliente.iniciar();
            } else if (tipologin instanceof ClienteVIP) {
                FachadaCliente fachadaCliente = fachada.getFachadaCliente();
                TelaCliente telaCliente = new TelaCliente(fachadaCliente,tipologin,fachada);
                telaCliente.iniciar();
            } else if (tipologin ==null){
                throw new ClienteNaoEncontradoException();
            }
        }
        System.out.println("Logout Concluido!");
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
            }
            return dado;
        }

    }
}



