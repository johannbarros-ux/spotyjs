import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cantor extends Usuario {
    private final String nomeArtistico;
    private final List<Musica> musicas = new ArrayList<>();
    private final List<Playlist> playlists = new ArrayList<>();

    public Cantor(int id, String nome, String email, String senhaHash, Plano plano, String nomeArtistico) {
        super(id, nome, email, senhaHash, plano);
        if (nomeArtistico == null || nomeArtistico.isBlank()) throw new IllegalArgumentException("Informe o nome artistico.");
        this.nomeArtistico = nomeArtistico.trim();
    }
    public boolean enviarMusica(Musica musica) {
        if (musica == null || !musica.validarFormato()) return false;
        if (!musicas.contains(musica)) {
            musica.setArtista(nomeArtistico);
            musicas.add(musica);
        }
        return true;
    }
    public Playlist criarPlaylist(String nome) {
        Playlist playlist = new Playlist(nome, this);
        playlists.add(playlist);
        return playlist;
    }
    public void adicionarPlaylist(Playlist playlist) {
        if (playlist != null && playlist.getProprietario() == this && !playlists.contains(playlist)) playlists.add(playlist);
    }
    public String getNomeArtistico() { return nomeArtistico; }
    public List<Musica> getMusicas() { return Collections.unmodifiableList(musicas); }
    public List<Playlist> getPlaylists() { return Collections.unmodifiableList(playlists); }
}