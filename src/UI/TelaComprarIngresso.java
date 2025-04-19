package UI;

import fachada.FachadaCliente;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import negocio.entidades.Cliente;
import negocio.exceptions.filmes.FilmeNaoEstaCadastradoException;
import negocio.exceptions.sessoes.SessaoNaoEncontradaException;

public class TelaComprarIngresso {
    private FachadaCliente fachada;
    private Scanner scanner;
    private Cliente cliente;

    public TelaComprarIngresso(FachadaCliente fachada, Cliente cliente) {
        this.fachada = fachada;
        this.scanner = new Scanner(System.in);
        this.cliente = cliente;
    }

    public void iniciar(){

        String dia;
        String horario;
        String filme;

        System.out.println("Escolha o dia,horario e filme   (digite 0 a qualquer momento para sair)");
        while(true){
            filme = lerDado("Nome do Filme");
            if (filme== null)return;
            try {
                fachada.consultarFilme(filme);
            } catch (FilmeNaoEstaCadastradoException e) {
                System.err.println(e.getMessage());
                return;
            }
            horario = lerHorario();
            if (horario== null)return;
            dia = lerData();
            if (dia== null)return;

            try {
                String sessao = fachada.procurarSessao(horario,filme,dia);
                TelaEscolhadeAssentos telaEscolhadeAssentos = new TelaEscolhadeAssentos(fachada,cliente,sessao);
                //telaEscolhadeAssentos.iniciar();
            } catch (SessaoNaoEncontradaException | FilmeNaoEstaCadastradoException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private String lerHorario() {
        String horario;
        do {
            System.out.println("Horário da sessão (HH:mm):");
            horario = scanner.nextLine();
            if(horario.equals("0")){
                System.out.println("Operação cancelada.");
                return null;
            }
            if (!isHorarioValido(horario)) {
                System.err.println("Horário inválido! Use formato HH:mm");
            }

        } while (!isHorarioValido(horario));

        return horario;
    }

    private boolean isHorarioValido(String horario) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime.parse(horario, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private String lerData() {
        String data;
        do {
            System.out.println("Data da sessão (dd-MM):");
            data = scanner.nextLine();
            if (data.equals("0")) {
                System.out.println("Operação cancelada.");
                return null;
            }

            if (!isDataValida(data)) {
                System.err.println("Data inválida! Use formato dd/MM");
            }

        } while (!isDataValida(data));

        return data;
    }

    private boolean isDataValida(String data) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM");
            MonthDay.parse(data, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
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
