public class Main {
    public static void main(String[] args) {
        Plano gratuito = new Plano("Gratuito", false, 6);
        Cantor cantor = new Cantor(
                1, "Ana", "ana@spotyjs.com", "senha", gratuito, "Ana Music");
        Ouvinte ouvinte = new Ouvinte(
                2, "Joao", "joao@spotyjs.com", "senha", gratuito);

        Musica musica = new Musica(101, "Primeira faixa", 210, "mp3");
        cantor.enviarMusica(musica);

        Playlist playlist = ouvinte.criarPlaylist("Favoritas");
        playlist.adicionarMusica(musica);
        ouvinte.curtir(musica);

        FilaDeReproducao fila = new FilaDeReproducao();
        fila.adicionar(musica);
        Player player = new Player();
        player.reproduzir(fila.obterAtual());

        System.out.println("SpotyJS iniciado");
        System.out.println("Playlist: " + playlist.getNome());
        System.out.println("Tocando: " + musica.getTitulo());
    }
}
