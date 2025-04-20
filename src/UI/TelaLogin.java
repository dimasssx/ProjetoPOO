package UI;

import java.util.Scanner;

import fachada.FachadaCliente;
import fachada.Movietime;
import negocio.entidades.Cliente;
import negocio.entidades.ClientePadrao;
import negocio.entidades.ClienteVIP;
import negocio.exceptions.usuario.ClienteNaoEncontradoException;
import negocio.exceptions.usuario.SenhaInvalidaException;
import negocio.exceptions.usuario.UsuarioJaExisteException;

public class TelaLogin {

    private Scanner scanner;
    private Movietime fachada;

    public TelaLogin(Movietime fachada) {
        this.scanner = new Scanner(System.in);
        this.fachada = fachada;
    }

    public void iniciar() {
        while (true) {
            System.out.println("\n------------------------------------");
            System.out.println("Faça o seu Login ou Cadastre-se!");
            System.out.println("------------------------------------");
            System.out.println("\n1 - Fazer Login");
            System.out.println("2 - Não possui uma conta? Cadastre-se");
            System.out.println("3 - Sair");
            
            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1":
                    try {
                        fazerLogin();
                    } catch (ClienteNaoEncontradoException e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case "2":
                    try {
                        cadastrarCliente();
                    } catch (UsuarioJaExisteException | SenhaInvalidaException e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case "3":
                    System.out.println("Saindo...");
                    return;
                default:
                    System.err.println("Opção Inválida");
            }
        }
    }

    public void cadastrarCliente() throws UsuarioJaExisteException, SenhaInvalidaException {
        System.out.println("------------------------------------");
        System.out.println("Cadastro (Digite 0 a qualquer momento para sair)");
        System.out.println("------------------------------------");
        String nomeDeUsuario = lerDado("Nome de Usuário (será utilizado para realizar seu login)");
        if (nomeDeUsuario == null) return;
        String senha = lerDado("Senha");
        if (senha == null) return;
        String nome = lerDado("Nome");
        if (nome == null) return;

        fachada.cadastrarCliente(nome, nomeDeUsuario, senha);
        System.out.println("\033[92m Cadastro Realizado! \033[0m");
    }

    public void fazerLogin() throws ClienteNaoEncontradoException {
        System.out.println("------------------------------------");
        System.out.println("Login (Digite 0 a qualquer momento para sair)");
        System.out.println("------------------------------------");
        String nomeDeUsuario = lerDado("Nome de usuário");
        if (nomeDeUsuario == null) return;
        String senha = lerDado("Senha");
        if (senha == null) return;
        checarCredenciais(nomeDeUsuario, senha);
    }

    public void checarCredenciais(String nomeDeUsuario, String senha) throws ClienteNaoEncontradoException {
        if (nomeDeUsuario.equals("admin") && senha.equals("admin123")) {
            TelaPrincipalGerente telaPrincipalGerente = new TelaPrincipalGerente(fachada.getFachadaGerente());
            telaPrincipalGerente.iniciar();
        } else {
            Cliente tipologin = fachada.autenticar(nomeDeUsuario, senha);
            if (tipologin instanceof ClientePadrao) {
                FachadaCliente fachadaCliente = fachada.getFachadaCliente();
                TelaPrincipalCliente telaPrincipalCliente = new TelaPrincipalCliente(fachadaCliente, tipologin, fachada);
                telaPrincipalCliente.iniciar();
            } else if (tipologin instanceof ClienteVIP) {
                FachadaCliente fachadaCliente = fachada.getFachadaCliente();
                TelaPrincipalCliente telaPrincipalCliente = new TelaPrincipalCliente(fachadaCliente, tipologin, fachada);
                telaPrincipalCliente.iniciar();
            } else if (tipologin == null) {
                throw new ClienteNaoEncontradoException();
            }
        }
        System.out.println("\033[92m Logout Concluído! \033[0m");
    }

    private String lerDado(String campo) {
        System.out.print(campo + ": ");
        while (true) {
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



