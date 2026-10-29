public class Playlist {
    private final String nome;
    private final java.util.List<Musica> musicas = new java.util.ArrayList<>();
    private final Usuario proprietario;

    public Playlist(String nome, Usuario proprietario) {
        this.nome = nome;
        this.proprietario = proprietario;
    }

    public void adicionarMusica(Musica musica) {
        if (musica != null && !musicas.contains(musica)) {
            musicas.add(musica);
        }
    }

    public void removerMusica(Musica musica) {
        musicas.remove(musica);
    }

    public void reordenar(int origem, int destino) {
        if (origem < 0 || origem >= musicas.size() || destino < 0 || destino >= musicas.size()) {
            throw new IndexOutOfBoundsException("Posicao invalida na playlist");
        }
        musicas.add(destino, musicas.remove(origem));
    }

    public String getNome() {
        return nome;
    }

    public java.util.List<Musica> getMusicas() {
        return java.util.Collections.unmodifiableList(musicas);
    }

    public Usuario getProprietario() {
        return proprietario;
    }
}
