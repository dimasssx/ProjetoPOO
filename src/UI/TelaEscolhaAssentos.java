package UI;

import java.util.Scanner;

import negocio.exceptions.SessaoNaoEncontradaException;
import negocio.entidades.*;
import negocio.SessoesNegocio;
import fachada.FachadaCliente;
import negocio.exceptions.AssentoIndisponivelException;


public class TelaEscolhaAssentos {
    private final Scanner scanner;
    private Sessao sessao;
    private int quantidadeIngressos;
    FachadaCliente fachada;

    public TelaEscolhaAssentos(Sessao sessao, FachadaCliente fachada) {
        this.sessao = sessao;
        this.scanner = new Scanner(System.in);
        this.fachada = fachada;
    }

    public void iniciar() throws SessaoNaoEncontradaException, AssentoIndisponivelException {
        try {
            fachada.mostrarAssentosDaSessao(sessao);
        } catch (SessaoNaoEncontradaException e) {
            System.err.println(e.getMessage());
        }

        String poltrona = null;
        try {
            System.out.println("Escolha a poltrona: (Ex: A2)");
            poltrona = scanner.nextLine();
            fachada.reservarAssento(sessao, poltrona);
        } catch (AssentoIndisponivelException e) {
            System.err.println(e.getMessage());
        }
    }
}