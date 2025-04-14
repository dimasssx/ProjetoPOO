package dados;

import negocio.entidades.Sala;
import java.util.List;

public interface IRepositorioSalas {
    void adicionarSala(Sala sala);
    void removerSala(Sala sala) ;
    void atualizarSala(Sala sala);
    Sala procurarSala(String codigo);
    List<Sala> listarSalas();
    void imprimir();
    boolean existe(Sala sala);
}