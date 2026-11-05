import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ouvinte extends Usuario {
    private final List<Musica> curtidas = new ArrayList<>();
    private final List<Playlist> playlists = new ArrayList<>();

    public Ouvinte(int id, String nome, String email, String senhaHash, Plano plano) {
        super(id, nome, email, senhaHash, plano);
    }
    public void curtir(Musica musica) {
        if (musica != null && !curtidas.contains(musica)) curtidas.add(musica);
    }
    public void descurtir(Musica musica) { curtidas.remove(musica); }
    public Playlist criarPlaylist(String nome) {
        Playlist playlist = new Playlist(nome, this);
        playlists.add(playlist);
        return playlist;
    }
    public void adicionarPlaylist(Playlist playlist) {
        if (playlist != null && playlist.getProprietario() == this && !playlists.contains(playlist)) playlists.add(playlist);
    }
    public boolean baixar(Musica musica) {
        return musica != null && getPlano() != null && getPlano().permiteDownload() && musica.possuiArquivo();
    }
    public List<Musica> getCurtidas() { return Collections.unmodifiableList(curtidas); }
    public List<Playlist> getPlaylists() { return Collections.unmodifiableList(playlists); }
}