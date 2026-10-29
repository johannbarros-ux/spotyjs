public class Cantor extends Usuario {
    private final String nomeArtistico;
    private final java.util.List<Musica> musicas = new java.util.ArrayList<>();
    private final java.util.List<Playlist> playlists = new java.util.ArrayList<>();

    public Cantor(int id, String nome, String email, String senhaHash, Plano plano, String nomeArtistico) {
        super(id, nome, email, senhaHash, plano);
        this.nomeArtistico = nomeArtistico;
    }

    public boolean enviarMusica(Musica musica) {
        if (musica == null || !musica.validarFormato()) {
            return false;
        }
        if (!musicas.contains(musica)) {
            musicas.add(musica);
        }
        return true;
    }

    public Playlist criarPlaylist(String nome) {
        Playlist playlist = new Playlist(nome, this);
        playlists.add(playlist);
        return playlist;
    }

    public String getNomeArtistico() {
        return nomeArtistico;
    }

    public java.util.List<Musica> getMusicas() {
        return java.util.Collections.unmodifiableList(musicas);
    }
}
