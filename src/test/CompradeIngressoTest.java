//TESTE IMPLEMENTADO POR DIMAS

package test;

import UI.TelaComprarIngresso;
import UI.TelaEscolhadeAssentos;
import UI.Utils.ValidacaoEntradas;
import dados.RepositorioClientesArquivoBinario;
import dados.RepositorioFilmesArquivoBinario;
import dados.RepositorioSessoesArquivoBinario;
import fachada.FachadaCliente;
import negocio.entidades.*;
import negocio.exceptions.assentos.AssentoIndisponivelException;
import negocio.exceptions.assentos.AssentoOcupadoException;
import org.junit.jupiter.api.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.MonthDay;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class CompradeIngressoTest{

    private FachadaCliente fachada;
    private Cliente clientePadrao;
    private Filme filme;
    private Sala sala;
    private Sessao sessao;
    private RepositorioSessoesArquivoBinario repositorioSessoes;
    private RepositorioFilmesArquivoBinario repositorioFilmes;
    private RepositorioClientesArquivoBinario repositorioClientes;

    // Arquivos com nomes fixos para os testes
    private File fileSessoes;
    private File fileFilmes;
    private File fileClientes;

    @BeforeEach
    public void setup() throws IOException {

        fileSessoes = new File("sessoestest.bin");
        fileFilmes = new File("filmetest.bin");
        fileClientes = new File("clientestest.bin");
        fileSessoes.createNewFile();
        fileFilmes.createNewFile();
        fileClientes.createNewFile();
        repositorioSessoes = new RepositorioSessoesArquivoBinario(fileSessoes.getPath());
        repositorioFilmes = new RepositorioFilmesArquivoBinario(fileFilmes.getPath());
        repositorioClientes = new RepositorioClientesArquivoBinario(fileClientes.getPath());

        fachada = new FachadaCliente(repositorioClientes, repositorioFilmes, repositorioSessoes);

        clientePadrao = new ClientePadrao("clientetest", "user", "123");
        filme = new Filme("batman", "heroi", "2h", "16");
        sala = new Sala2D("sala 3", 10, 12);
        sessao = new Sessao(filme, "13:15", sala, "24-04");

        repositorioFilmes.adicionarFilme(filme);
        repositorioSessoes.adicionarSessao(sessao);
    }
    @AfterEach
    public void deletar() {
        if (fileSessoes.exists()) fileSessoes.delete();
        if (fileFilmes.exists()) fileFilmes.delete();
        if (fileClientes.exists()) fileClientes.delete();
    }

    @Test
    public void testCompraIngressoComSucesso() {

        String idSessao = sessao.getId();
        sessao.setHorario(LocalTime.now().plusMinutes(10));
        sessao.setDia(MonthDay.now());

        String inputSimulado = idSessao + "\n" + // ID da sessão
                "1\n" + // Quantidade de ingressos
                "h3\n" + // Assento
                "1\n" + // Tipo de ingresso (Inteira)
                "N\n" + // Não quer lanches
                "3\n" // Forma de pagamento (PIX)
         ;
        Scanner scannerSimulado = new Scanner(inputSimulado);
        ValidacaoEntradas.setScanner(scannerSimulado);

        TelaComprarIngresso telaComprar = new TelaComprarIngresso(fachada, clientePadrao, scannerSimulado);
        telaComprar.iniciar();

        assertEquals(1, clientePadrao.getIngressosComprados().size());
        Ingresso ingressoComprado = clientePadrao.getIngressosComprados().getFirst();
        assertEquals(idSessao, ingressoComprado.getSessao().getId());
        assertEquals("h3".toUpperCase(), ingressoComprado.getAssento().toString());
    }
    @Test
    public void testCompraIngressoAssentoOcupado() {String idSessao = sessao.getId();
        sessao.setHorario(LocalTime.now().plusMinutes(10));
        sessao.setDia(MonthDay.now());
        String inputSimulado = idSessao + "\n" + // ID da sessão
                "1\n" + // Quantidade de ingressos
                "h3\n" + // Assento
                "1\n" + // Tipo de ingresso (Inteira)
                "N\n" + // Não quer lanches
                "3\n" // Forma de pagamento (PIX)
                ;
        Scanner scannerSimulado = new Scanner(inputSimulado);
        ValidacaoEntradas.setScanner(scannerSimulado);
        TelaComprarIngresso telaComprar = new TelaComprarIngresso(fachada, clientePadrao, scannerSimulado);
        telaComprar.iniciar();

        //segunda tentativa de comprar o mesmo assento

        inputSimulado = idSessao + "\n" + // ID da sessão
                "1\n" + // Quantidade de ingressos
                "H3\n" + // Assento que já foi escolhido
                "0\n";
        Scanner scannerSimulado2 = new Scanner(inputSimulado);
        ValidacaoEntradas.setScanner(scannerSimulado2);
        PrintStream originalErr = System.err;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));
        try {
            TelaComprarIngresso telaComprar2 = new TelaComprarIngresso(fachada, clientePadrao,scannerSimulado2);
            telaComprar2.iniciar();
        } catch (Exception e) {
            // ignorar erros do Scanner
        } finally {
            System.setErr(originalErr);
        }
        String output = outContent.toString();
        assertTrue(output.contains("Este assento já está reservado!"), "Esperava mensagem de assento ocupado");
        assertEquals(1,clientePadrao.getIngressosComprados().size());
    }

    @Test
    public void testFormadePagamentoInvalido() {

        String idSessao = sessao.getId();
        sessao.setHorario(LocalTime.now().plusMinutes(10));
        sessao.setDia(MonthDay.now());

        String inputSimulado = idSessao + "\n" + // ID da sessão
                "1\n" + // Quantidade de ingressos
                "h3\n" + // Assento
                "1\n" + // Tipo de ingresso (Inteira)
                "N\n" + // Não quer lanches
                "\n" // Forma de pagamento (PIX)
                +"0\n";
        Scanner scannerSimulado = new Scanner(inputSimulado);
        ValidacaoEntradas.setScanner(scannerSimulado);

        PrintStream originalErr = System.err;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));
        try {
            TelaComprarIngresso telaComprar = new TelaComprarIngresso(fachada, clientePadrao, scannerSimulado);
            telaComprar.iniciar();
        } catch (Exception e) {
            // ignorar erros do Scanner
        } finally {
            System.setErr(originalErr);
        }
        String output = outContent.toString();
        assertTrue(output.contains("Método de pagamento inválido. Por favor, escolha uma opção válida."), "Esperava mensagem de opção inválida");
        assertEquals(0,clientePadrao.getIngressosComprados().size());


    }
}

