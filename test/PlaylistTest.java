import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PlaylistTest {
    @Test void adicionaEReordenaMusicas() {
        Ouvinte dono = new Ouvinte(1, "Ana", "ana@example.com", PasswordUtils.gerarHash("senha123"), new Plano("Gratuito", false, 6));
        Playlist playlist = dono.criarPlaylist("Favoritas");
        Musica a = new Musica(1, "Faixa A", 100, "mp3");
        Musica b = new Musica(2, "Faixa B", 120, "mp3");
        playlist.adicionarMusica(a); playlist.adicionarMusica(b); playlist.adicionarMusica(a);
        assertEquals(2, playlist.getMusicas().size());
        playlist.reordenar(0, 1);
        assertEquals("Faixa B", playlist.getMusicas().get(0).getTitulo());
    }
}