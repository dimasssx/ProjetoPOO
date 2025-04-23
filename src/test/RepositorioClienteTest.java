//Teste implementado por José Portela

package test;
import dados.RepositorioClientesArquivoBinario;
import negocio.ClientesNegocio;
import negocio.entidades.*;
import negocio.exceptions.usuario.ClienteNaoEncontradoException;
import negocio.exceptions.usuario.UsuarioJaExisteException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

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
    void testAdicionarComSucessoEListar() throws Exception{
        Cliente clienteTeste = new ClientePadrao("José Portela", "teste", "teste123");
        repClientes.adicionarCliente(clienteTeste);
        ArrayList<Cliente> clientes = repClientes.listarClientes();
        assertEquals(1, clientes.size());
        assertEquals("José Portela", clientes.get(0).getNome());
    }

    //adicionar cliente com o mesmo nome de usuario que já existe, nao é válido
    @Test
    void testFalharAoAdicionarEListar() throws Exception {
        Cliente clienteTeste = new ClientePadrao("José Portela", "teste", "teste123");
        repClientes.adicionarCliente(clienteTeste);
        //a verificação de erro esta em negocio, pois no repositorio não se faz verificações, apenas adiciona de forma crua
        assertThrows(UsuarioJaExisteException.class, () -> clienteNegocio.adicionarCliente("José Portela", "teste", "teste123"));
    }

    @Test
    void testRetornarCliente() throws Exception{
        Cliente cliente = new ClientePadrao("José Portela", "teste", "teste123");
        repClientes.adicionarCliente(cliente);
        //buscar cliente de negocio utiliza o retornar cliente do rep
        Cliente clienteTeste = repClientes.retornarCliente(cliente.getNomeDeUsuario(), cliente.getSenha());
        //ver se esta retornando o mesmo cliente
        assertEquals("José Portela", clienteTeste.getNome());
        assertEquals("teste", clienteTeste.getNomeDeUsuario());
        assertEquals("teste123", clienteTeste.getSenha());
    }

    @Test
    void testFalhaAoRetornarCliente() throws Exception{
        Cliente cliente = new ClientePadrao("José Portela", "teste", "teste123");
        repClientes.adicionarCliente(cliente);
        //vai dar erro ao buscar o cliente com credenciais incorretas
        Cliente clienteTeste = repClientes.retornarCliente("testeErro", "teste543");
        assertNull(clienteTeste);
    }

    @Test
    void testRemoverCliente() throws Exception{
        Cliente cliente = new ClientePadrao("José Portela", "teste", "teste123");
        repClientes.adicionarCliente(cliente);
        Cliente clienteTeste = repClientes.retornarCliente("teste", "teste123");
        repClientes.removerCliente(clienteTeste);
        ArrayList<Cliente> clientes = repClientes.listarClientes();
        assertEquals(0, clientes.size());

    }

    @Test
    void testAtualizarCliente() throws Exception{
        Cliente cliente = new ClientePadrao("José Portela", "teste", "teste123");
        repClientes.adicionarCliente(cliente);
        Cliente clienteTeste = repClientes.retornarCliente("teste", "teste123");
        //alterar senha utiliza de atualizar cliente do repositorio
        clienteNegocio.alterarSenha(clienteTeste, "teste543");
        assertEquals("teste543", clienteTeste.getSenha());
    }
}
