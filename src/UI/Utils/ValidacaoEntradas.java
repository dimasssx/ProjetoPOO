package UI.Utils;

import java.time.LocalTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ValidacaoEntradas {

    // cores para melhorar a interface, vao estar presentes apenas nos prints da tela para mudar a cor
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BOLD = "\u001B[1m";

    private static  Scanner scanner = new Scanner(System.in);

    public static String lerDado(String campo) {
        System.out.print(ANSI_BOLD+ campo + ": "+ANSI_RESET);
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
            if(campo.equals("Duração")){
                String duracaoValida = verificarDuracao(dado);
                if (duracaoValida == null){
                    continue;
                }
                return duracaoValida;
            }
            if(campo.equals("Classificação")){
                String classvalida = verificarClassificacao(dado);
                if (classvalida == null){
                    continue;
                }
                return classvalida;
            }if(campo.equals("Deseja comprar lanches? (S/N)")){
                if (!(dado.equalsIgnoreCase("S")) && !(dado.equalsIgnoreCase("N"))){
                    System.err.println("Entrada Inválida");
                    continue;
                }
                else return dado;
            }
            return dado;
        }
    }
    public static String lerHorario() {
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
    public static boolean isHorarioValido(String horario) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime.parse(horario, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    public static String lerData() {
        String data;
        do {
            System.out.println(ANSI_BOLD + "Digite a data da sessão (dd-MM): " + ANSI_RESET);
            data = scanner.nextLine();
            if (data.equals("0")) {
                System.out.println(ANSI_YELLOW + "Operação cancelada." + ANSI_RESET);
                return null;
            }

            if (!isDataValida(data)) {
                System.err.println("Formato inválido! Use dd-MM (ex: 17-04).");
            }

        } while (!isDataValida(data));

        return data;
    }
    public static boolean isDataValida(String data) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM");
            MonthDay.parse(data, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    public static String verificarClassificacao(String dado) {
        Set<String> classificacoesValidas = new HashSet<>();
        classificacoesValidas.add("Livre");
        classificacoesValidas.add("10");
        classificacoesValidas.add("12");
        classificacoesValidas.add("14");
        classificacoesValidas.add("16");
        classificacoesValidas.add("18");

        if (classificacoesValidas.contains(dado.trim())) {
            return dado.trim();
        }
        System.err.println("Classificação inválida! As opções válidas são: Livre, 10, 12, 14, 16, 18");
        return null;
    }
    private static String verificarDuracao(String dado) {
        if (!dado.matches("\\dh\\d{2}|\\dh")) {
            System.err.println("Formato inválido! Deve ser no formato xhxx ou xh.");
            return null;
        }
        if (dado.matches("\\dh\\d{2}")) {
            String minutosStr = dado.substring(dado.indexOf('h') + 1);
            int minutos = Integer.parseInt(minutosStr);
            if (minutos > 59) {
                System.err.println("Os minutos não podem ser maiores que 59.");
                return null;
            }
        }
        return dado;
    }
    //para testes
    public static void setScanner(Scanner scannerCustomizado) {
        scanner = scannerCustomizado;
    }

}
