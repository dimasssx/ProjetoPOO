package dados;

//import negocio.Exceptions.FilmeJaEstanoCatalogoException;
//import negocio.Exceptions.FilmeNaoEstaNoCatalogoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

import negocio.entidades.Filme;

public class RepositorioFilmesArquivoBinario implements IRepositorioFilmes,Serializable {

    @Serial
    private static final long serialVersionUID = -4009776605163947716L;
    private ArrayList<Filme> catalogo;
    private File file;

    public RepositorioFilmesArquivoBinario(){
        file = new File("filmes.dat");

        if (file.exists()){
            lerFilmes();
        }else {
            catalogo = new ArrayList<>();
            escritaFilmes();
        }
    }

    //ler o arquivo de filmes
    public void lerFilmes(){
        try(FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis)){
            catalogo = (ArrayList<Filme>) ois.readObject();
        }catch (IOException | ClassNotFoundException e){
            System.err.println(e.getMessage());
            catalogo = new ArrayList<Filme>();
        }
    }

    //escrever o arquivo de filmes
    public void escritaFilmes(){
        try(FileOutputStream fos = new FileOutputStream(file);
            ObjectOutput oos = new ObjectOutputStream(fos)){

            oos.writeObject(catalogo);
        }catch (IOException e){
            System.err.println(e.getMessage());
            catalogo = new ArrayList<Filme>();
        }
    }

    //adicionar um objeto de filme ao repositorio
    @Override
    public void adicionarFilme(Filme filme){
        catalogo.add(filme);
        escritaFilmes();
    }

    //remover um filme do repositorio a partir de um objeto de filmes recebido
    @Override
    public void removerFilme(Filme filme){
        int index = catalogo.indexOf(filme);
        if (index != -1){
            catalogo.remove(filme);
            escritaFilmes();
        }
    }
    //atualizar um filme do repositorio a partir de um objeto de filme recebido
    @Override
    public void atualizaFilme(Filme filme){
        int index = catalogo.indexOf(filme);
        if (index != -1){
            catalogo.set(index,filme);
            escritaFilmes();
        }
    }
    //procurar um filme no repositorio a partir do nome do filme recebido
    public Filme procurarFilme(String nome){
        Filme filmeDesejado = null;
        for (Filme filme: catalogo){
            if(filme.getTitulo().equalsIgnoreCase(nome)){
                filmeDesejado = filme;
                break;
            }
        }
        return filmeDesejado;
    }

    //listar todos os filmes do repositorio
    @Override
    public ArrayList<Filme> listarFilmes(){
        ArrayList<Filme> filmes = new ArrayList<>();
        for (Filme filme : catalogo) {
            if (filme != null) {
                filmes.add(filme);
            }
        }
        return filmes;
    }

    //verificar se um filme ja existe no repositorio a partir de um objeto de filme recebido
    @Override
    public boolean existe(Filme filme){
        return catalogo.contains(filme);
    }
}