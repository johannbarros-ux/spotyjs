import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class FilaDeReproducaoTest {
    @Test void avancaVoltaEReiniciaFila() {
        FilaDeReproducao fila = new FilaDeReproducao();
        Musica primeira = new Musica(1, "Primeira", 100, "mp3");
        Musica segunda = new Musica(2, "Segunda", 120, "mp3");
        fila.adicionar(primeira);
        fila.adicionar(segunda);
        assertEquals(primeira, fila.obterAtual());
        fila.avancar();
        assertEquals(segunda, fila.obterAtual());
        assertEquals(primeira, fila.voltar());
        fila.limpar();
        assertNull(fila.obterAtual());
        assertEquals(-1, fila.getPosicaoAtual());
    }
}