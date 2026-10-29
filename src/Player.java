public class Player {
    private int volume;
    private Reproduzivel conteudoAtual;

    public Player() {
        volume = 50;
    }

    public void reproduzir(Reproduzivel conteudo) {
        if (conteudo == null) {
            throw new IllegalArgumentException("O conteudo nao pode ser nulo");
        }
        conteudoAtual = conteudo;
        conteudoAtual.reproduzir();
    }

    public void pausar() {
        if (conteudoAtual != null) {
            conteudoAtual.pausar();
        }
    }

    public void avancar(FilaDeReproducao fila) {
        if (fila == null) {
            throw new IllegalArgumentException("A fila nao pode ser nula");
        }
        fila.avancar();
        Musica musica = fila.obterAtual();
        if (musica != null) {
            reproduzir(musica);
        }
    }

    public void retroceder(FilaDeReproducao fila) {
        if (fila == null) {
            throw new IllegalArgumentException("A fila nao pode ser nula");
        }
        Musica musica = fila.voltar();
        if (musica != null) {
            reproduzir(musica);
        }
    }

    public void ajustarVolume(int volume) {
        if (volume < 0 || volume > 100) {
            throw new IllegalArgumentException("O volume deve estar entre 0 e 100");
        }
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }

    public Reproduzivel getConteudoAtual() {
        return conteudoAtual;
    }
}
