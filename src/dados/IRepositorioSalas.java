package dados;

import negocio.entidades.Sala;

import java.util.ArrayList;

public interface IRepositorioSalas {
    void adicionarSala(Sala sala);
    void removerSala(Sala sala);
    void atualizarSala(Sala sala);
    Sala procurarSala(String codigo);
    ArrayList<Sala> listarSalas();
    boolean existe(Sala sala);
}