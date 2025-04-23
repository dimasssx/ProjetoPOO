package test;

import dados.RepositorioClientesArquivoBinario;
import negocio.ClientesNegocio;
import negocio.entidades.*;
import negocio.exceptions.usuario.ClienteNaoEncontradoException;
import negocio.exceptions.usuario.UsuarioJaExisteException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;

public class RepositorioClienteTest {
    private ClientesNegocio clienteNegocio;
    private RepositorioClientesArquivoBinario repClientes;
    private final String DIRETORIOARQUIVOCLIENTES = "clientesTest.bin";

    @BeforeEach
    void setUp() {
        repClientes = new RepositorioClientesArquivoBinario(DIRETORIOARQUIVOCLIENTES);
        clienteNegocio = new ClientesNegocio(repClientes);
    }

    @AfterEach
    void excluir() {
        File f = new File(DIRETORIOARQUIVOCLIENTES);
        if (f.exists()) f.delete();
    }

    @Test
    void testAdicionar() throws Exception{
        clienteNegocio.adicionarCliente("José Portela", "teste", "teste123");
        ArrayList<Cliente> clientes = repClientes.listarClientes();
        assertEquals(1, clientes.size());
        assertEquals("José Portela", clientes.get(0).getNome());
    }

    @Test
    void testFalharAdicionar() throws Exception {
        clienteNegocio.adicionarCliente("José Portela", "teste", "teste123");
        assertThrows(UsuarioJaExisteException.class, () -> clienteNegocio.adicionarCliente("José Portela", "teste", "teste321"));
    }

    @Test
    void testRetornarCliente() throws Exception{
        clienteNegocio.adicionarCliente("José Portela", "teste", "teste123");
        Cliente clienteTeste = clienteNegocio.buscarCliente("teste", "teste123");
        //ver se esta retornando o mesmo cliente
        assertEquals("José Portela", clienteTeste.getNome());
        assertEquals("teste", clienteTeste.getNomeDeUsuario());
        assertEquals("teste123", clienteTeste.getSenha());
    }

    @Test
    void testFalhaAoRetornarCliente() throws Exception{
        clienteNegocio.adicionarCliente("José Portela", "teste", "teste123");
        //vai dar erro ao buscar o cliente com credenciais incorretas
        assertThrows(ClienteNaoEncontradoException.class, () -> clienteNegocio.buscarCliente("testeErrado", "teste543"));
    }

    @Test
    void testRemoverCliente() throws Exception{
        clienteNegocio.adicionarCliente("José Portela", "teste", "teste123");
        Cliente clienteTeste = clienteNegocio.buscarCliente("teste", "teste123");
        repClientes.removerCliente(clienteTeste);
    }
}
