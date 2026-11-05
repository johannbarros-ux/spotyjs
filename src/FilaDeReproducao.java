import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilaDeReproducao {
    private final List<Musica> musicas = new ArrayList<>();
    private int posicaoAtual = -1;
    public void adicionar(Musica musica) {
        if (musica != null) {
            musicas.add(musica);
            if (posicaoAtual == -1) posicaoAtual = 0;
        }
    }
    public void limpar() { musicas.clear(); posicaoAtual = -1; }
    public void avancar() {
        if (posicaoAtual < musicas.size() - 1) posicaoAtual++;
    }
    public Musica voltar() {
        if (posicaoAtual > 0) posicaoAtual--;
        return obterAtual();
    }
    public void reordenar(int origem, int destino) {
        if (origem < 0 || origem >= musicas.size() || destino < 0 || destino >= musicas.size())
            throw new IndexOutOfBoundsException("Posicao invalida na fila.");
        Musica atual = obterAtual();
        Musica movida = musicas.remove(origem);
        musicas.add(destino, movida);
        posicaoAtual = musicas.indexOf(atual);
    }
    public Musica obterAtual() {
        return posicaoAtual >= 0 && posicaoAtual < musicas.size() ? musicas.get(posicaoAtual) : null;
    }
    public List<Musica> getMusicas() { return Collections.unmodifiableList(musicas); }
    public int getPosicaoAtual() { return posicaoAtual; }
}