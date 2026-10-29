public class FilaDeReproducao {
    private final java.util.List<Musica> musicas = new java.util.ArrayList<>();
    private int posicaoAtual = -1;

    public void adicionar(Musica musica) {
        if (musica != null) {
            musicas.add(musica);
            if (posicaoAtual == -1) {
                posicaoAtual = 0;
            }
        }
    }

    public void avancar() {
        if (posicaoAtual < musicas.size() - 1) {
            posicaoAtual++;
        }
    }

    public Musica voltar() {
        if (posicaoAtual > 0) {
            posicaoAtual--;
        }
        return obterAtual();
    }

    public void reordenar(int origem, int destino) {
        if (origem < 0 || origem >= musicas.size() || destino < 0 || destino >= musicas.size()) {
            throw new IndexOutOfBoundsException("Posicao invalida na fila");
        }
        musicas.add(destino, musicas.remove(origem));
        if (posicaoAtual == origem) {
            posicaoAtual = destino;
        }
    }

    public Musica obterAtual() {
        return posicaoAtual >= 0 ? musicas.get(posicaoAtual) : null;
    }

    public java.util.List<Musica> getMusicas() {
        return java.util.Collections.unmodifiableList(musicas);
    }

    public int getPosicaoAtual() {
        return posicaoAtual;
    }
}
