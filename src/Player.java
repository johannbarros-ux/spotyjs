import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

/** Player MP3 real usando JLayer. A reproducao ocorre em uma thread separada. */
public class Player implements Reproduzivel {
    private int volume = 50;
    private Reproduzivel conteudoAtual;
    private Musica musicaAtual;
    private AdvancedPlayer playerAudio;
    private Thread threadAudio;
    private volatile int quadroAtual;
    private volatile boolean pausado;

    public synchronized void reproduzir(Musica musica) {
        if (musica == null) throw new IllegalArgumentException("A musica nao pode ser nula.");
        pararAudio();
        conteudoAtual = null;
        musicaAtual = musica;
        quadroAtual = 0;
        iniciarArquivo(musica, 0);
    }

    public synchronized void reproduzir(Reproduzivel conteudo) {
        if (conteudo == null) throw new IllegalArgumentException("O conteudo nao pode ser nulo.");
        pararAudio();
        musicaAtual = null;
        conteudoAtual = conteudo;
        conteudoAtual.reproduzir();
    }

    private synchronized void iniciarArquivo(Musica musica, int quadroInicial) {
        String caminho = musica.getCaminhoArquivo();
        if (caminho == null || !Files.isRegularFile(Path.of(caminho))) {
            System.out.println("Arquivo de audio nao encontrado: " + caminho);
            return;
        }
        pausado = false;
        threadAudio = new Thread(() -> {
            try (FileInputStream entrada = new FileInputStream(caminho)) {
                AdvancedPlayer novoPlayer = new AdvancedPlayer(entrada);
                synchronized (this) { playerAudio = novoPlayer; }
                novoPlayer.setPlayBackListener(new PlaybackListener() {
                    @Override public void playbackFinished(PlaybackEvent evento) {
                        quadroAtual = evento.getFrame();
                    }
                });
                novoPlayer.play(quadroInicial, Integer.MAX_VALUE);
                quadroAtual = 0;
            } catch (Exception ex) {
                System.err.println("Falha ao reproduzir audio: " + ex.getMessage());
            } finally {
                synchronized (this) { playerAudio = null; }
            }
        }, "spotyjs-audio");
        threadAudio.setDaemon(true);
        threadAudio.start();
        System.out.println("Reproduzindo: " + musica.getTitulo());
    }

    @Override public synchronized void reproduzir() {
        if (musicaAtual != null) {
            if (pausado) iniciarArquivo(musicaAtual, quadroAtual);
            else if (playerAudio == null) iniciarArquivo(musicaAtual, 0);
        } else if (conteudoAtual != null) conteudoAtual.reproduzir();
    }

    @Override public synchronized void pausar() {
        if (playerAudio != null) {
            pausado = true;
            playerAudio.close();
            playerAudio = null;
            System.out.println("Reproducao pausada.");
        } else if (conteudoAtual != null) conteudoAtual.pausar();
    }

    public synchronized void parar() {
        pararAudio();
        quadroAtual = 0;
        pausado = false;
        musicaAtual = null;
        conteudoAtual = null;
    }

    private synchronized void pararAudio() {
        if (playerAudio != null) {
            playerAudio.close();
            playerAudio = null;
        }
        if (conteudoAtual != null) conteudoAtual.pausar();
    }

    public void avancar(FilaDeReproducao fila) {
        if (fila == null) throw new IllegalArgumentException("A fila nao pode ser nula.");
        fila.avancar();
        Musica musica = fila.obterAtual();
        if (musica != null) reproduzir(musica);
    }
    public void retroceder(FilaDeReproducao fila) {
        if (fila == null) throw new IllegalArgumentException("A fila nao pode ser nula.");
        Musica musica = fila.voltar();
        if (musica != null) reproduzir(musica);
    }
    public void ajustarVolume(int volume) {
        if (volume < 0 || volume > 100) throw new IllegalArgumentException("O volume deve estar entre 0 e 100.");
        this.volume = volume;
    }
    public int getVolume() { return volume; }
    public Reproduzivel getConteudoAtual() { return conteudoAtual; }
    public Musica getMusicaAtual() { return musicaAtual; }
    public boolean isPausado() { return pausado; }
}