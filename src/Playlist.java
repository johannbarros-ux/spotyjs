import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Playlist {
    private final String nome;
    private final List<Musica> musicas = new ArrayList<>();
    private final Usuario proprietario;

    public Playlist(String nome, Usuario proprietario) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Informe o nome da playlist.");
        if (proprietario == null) throw new IllegalArgumentException("Playlist precisa de proprietario.");
        this.nome = nome.trim();
        this.proprietario = proprietario;
    }
    public void adicionarMusica(Musica musica) {
        if (musica != null && !musicas.contains(musica)) musicas.add(musica);
    }
    public void removerMusica(Musica musica) { musicas.remove(musica); }
    public void reordenar(int origem, int destino) {
        if (origem < 0 || origem >= musicas.size() || destino < 0 || destino >= musicas.size())
            throw new IndexOutOfBoundsException("Posicao invalida na playlist.");
        musicas.add(destino, musicas.remove(origem));
    }
    public String getNome() { return nome; }
    public List<Musica> getMusicas() { return Collections.unmodifiableList(musicas); }
    public Usuario getProprietario() { return proprietario; }
}