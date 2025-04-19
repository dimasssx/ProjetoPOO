package dados;

import java.util.ArrayList;

import negocio.entidades.Sala;

public interface IRepositorioSalas {
    void adicionarSala(Sala sala);
    void removerSala(Sala sala);
    void atualizarSala(Sala sala);
    Sala procurarSala(String codigo);
    ArrayList<Sala> listarSalas();
    boolean existe(Sala sala);
}