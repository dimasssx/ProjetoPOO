package UI;

import fachada.FachadaCliente;
import fachada.Movietime;
import negocio.entidades.Cliente;
import negocio.entidades.ClientePadrao;
import negocio.entidades.ClienteVIP;
import java.util.Scanner;
import negocio.exceptions.usuario.ClienteNaoEncontradoException;
import negocio.exceptions.usuario.SenhaInvalidaException;
import negocio.exceptions.usuario.UsuarioJaExisteException;
import static UI.Utils.ValidacaoEntradas.*;

public class TelaLogin {

    private final Scanner scanner;
    private final Movietime fachada;

    // cores ANSI para melhorar a interface, vao estar presentes nos prints da tela para mudar a cor
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BOLD = "\u001B[1m";

    public TelaLogin(Movietime fachada) {
        this.scanner = new Scanner(System.in);
        this.fachada = fachada;
    }

    public void iniciar() {
        while (true) {
            System.out.println("\n" + ANSI_BOLD);
            System.out.println("╔══════════════════════════════════════════════╗");
            System.out.println("║               MOVIETIME CINEMA               ║");
            System.out.println("╚══════════════════════════════════════════════╝" + ANSI_RESET);
            
            System.out.println(ANSI_BOLD + "═════════════════════════════════════════" + ANSI_RESET);
            System.out.println(ANSI_BOLD + "  ACESSO AO SISTEMA" + ANSI_RESET);
            System.out.println(ANSI_BOLD + "═════════════════════════════════════════" + ANSI_RESET);
            
            System.out.println("1 - " + "Fazer Login" + ANSI_RESET);
            System.out.println("2 - " + "Não possui uma conta? Cadastre-se" + ANSI_RESET);
            System.out.println("3 - " + "Encerrar" + ANSI_RESET);
            
            System.out.print("► ");
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
                        System.out.println(ANSI_RED + e.getMessage() + ANSI_RESET);
                    }
                    break;
                case "3":
                    System.out.println(ANSI_YELLOW + "Saindo do sistema..." + ANSI_RESET);
                    return;
                default:
                    System.err.println("Opção inválida. Por favor, tente novamente.");
            }
        }
    }

    public void cadastrarCliente() throws UsuarioJaExisteException, SenhaInvalidaException {
        System.out.println("\n" + ANSI_BOLD);
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║                  CADASTRO                    ║");
        System.out.println("╚══════════════════════════════════════════════╝" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "Digite 0 a qualquer momento para cancelar" + ANSI_RESET);
        
        String nomeDeUsuario = lerDado("Nome de Usuário (será utilizado para realizar seu login)");
        if (nomeDeUsuario == null) return;

        String senha = lerDado("Senha");
        if (senha == null) return;
        
        String nome = lerDado("Nome");
        if (nome == null) return;

        fachada.cadastrarCliente(nome, nomeDeUsuario, senha);
        
        System.out.println("\n" + ANSI_GREEN + ANSI_BOLD);
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║           CADASTRO REALIZADO COM             ║");
        System.out.println("║                  SUCESSO!                    ║");
        System.out.println("╚══════════════════════════════════════════════╝" + ANSI_RESET);
    }

    public void fazerLogin() throws ClienteNaoEncontradoException {
        System.out.println("\n" + ANSI_BOLD);
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║                   LOGIN                      ║");
        System.out.println("╚══════════════════════════════════════════════╝" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "Digite 0 a qualquer momento para cancelar" + ANSI_RESET);
        
        String nomeDeUsuario = lerDado("Nome de usuário");
        if (nomeDeUsuario == null) return;
        String senha = lerDado("Senha");
        if (senha == null) return;
        checarCredenciais(nomeDeUsuario, senha);
    }

    public void checarCredenciais(String nomeDeUsuario, String senha) throws ClienteNaoEncontradoException {
        //verifica se é o gerente, pelo login fixo do gerente
        if (nomeDeUsuario.equals("admin") && senha.equals("admin123")) {
            System.out.println(ANSI_BOLD + "Bem-vindo, Gerente!" + ANSI_RESET);
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
        System.out.println(ANSI_GREEN + ANSI_BOLD + "Logout concluído com sucesso!" + ANSI_RESET);
    }
}