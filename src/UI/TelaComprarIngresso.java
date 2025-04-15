public class TelaComprarIngresso {

    private FachadaCliente fachada;
    private Scanner scanner;

    public TelaComprarIngresso(FachadaCliente fachada) {
        this.fachada = fachada;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() throws AssentoIndisponivelException, SessaoNaoEncontradaException {

        Sessao sessaoescolhida = null;
        while (sessaoescolhida == null) {

            System.out.println("Dia desejado dd-MM");
            String dia = scanner.nextLine();
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM");
                MonthDay day = MonthDay.parse(dia, formatter);
            } catch (DateTimeParseException e) {
                System.err.println("Formato de data invalido, usar dd-MM");
                return;
            }

            System.out.println("Filme desejado");
            String nome = scanner.nextLine();


            System.out.println("horario desejado hh:mm");
            String horario = scanner.nextLine();
            LocalTime hhorario;
            try {
                hhorario = LocalTime.parse(horario);
            } catch (DateTimeParseException e) {
                System.err.println("Erro: Formato de horário inválido. Use hh:mm.");
                return;
            }

            try {
                sessaoescolhida = fachada.checarSessao(dia, nome, horario);
            } catch (Exception e) {
                System.err.println(e);
                ;
            }
        }

        int quantidade_ingressos;

        System.out.println("Quantos ingressos deseja comprar? ");
        quantidade_ingressos = scanner.nextInt();
        scanner.nextLine();
        int i = 0;
        while (i < quantidade_ingressos) {
            TelaEscolhaAssentos telaAssentosSala = new TelaEscolhaAssentos(sessaoescolhida, fachada);
            telaAssentosSala.iniciar();
            i++;
        }

    }
}