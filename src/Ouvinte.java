public class Ouvinte extends Usuario {
    private final java.util.List<Musica> curtidas = new java.util.ArrayList<>();
    private final java.util.List<Playlist> playlists = new java.util.ArrayList<>();

    public Ouvinte(int id, String nome, String email, String senhaHash, Plano plano) {
        super(id, nome, email, senhaHash, plano);
    }

    public void curtir(Musica musica) {
        if (musica != null && !curtidas.contains(musica)) {
            curtidas.add(musica);
        }
    }

    public Playlist criarPlaylist(String nome) {
        Playlist playlist = new Playlist(nome, this);
        playlists.add(playlist);
        return playlist;
    }

    public boolean baixar(Musica musica) {
        return musica != null && getPlano() != null && getPlano().permiteDownload();
    }

    public java.util.List<Musica> getCurtidas() {
        return java.util.Collections.unmodifiableList(curtidas);
    }

    public java.util.List<Playlist> getPlaylists() {
        return java.util.Collections.unmodifiableList(playlists);
    }
}
