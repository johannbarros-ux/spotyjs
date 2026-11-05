import java.nio.file.Files;
import java.nio.file.Path;

public class Musica {
    private final int id;
    private final String titulo;
    private final int duracaoSegundos;
    private final String formato;
    private String caminhoArquivo;
    private String artista;

    public Musica(int id, String titulo, int duracaoSegundos, String formato) {
        this(id, titulo, duracaoSegundos, formato, null, "Artista independente");
    }

    public Musica(int id, String titulo, int duracaoSegundos, String formato, String caminhoArquivo, String artista) {
        if (id <= 0) throw new IllegalArgumentException("ID da musica invalido.");
        if (titulo == null || titulo.isBlank()) throw new IllegalArgumentException("Titulo obrigatorio.");
        if (duracaoSegundos <= 0) throw new IllegalArgumentException("A duracao deve ser positiva.");
        this.id = id;
        this.titulo = titulo.trim();
        this.duracaoSegundos = duracaoSegundos;
        this.formato = formato == null ? "" : formato.trim().toLowerCase();
        this.caminhoArquivo = caminhoArquivo;
        this.artista = artista == null || artista.isBlank() ? "Artista independente" : artista.trim();
    }

    public int obterDuracao() { return duracaoSegundos; }
    public boolean validarFormato() {
        return formato.equals("mp3") || formato.equals("wav") || formato.equals("ogg");
    }
    public boolean possuiArquivo() {
        return caminhoArquivo != null && Files.isRegularFile(Path.of(caminhoArquivo));
    }
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getFormato() { return formato; }
    public String getCaminhoArquivo() { return caminhoArquivo; }
    public void setCaminhoArquivo(String caminhoArquivo) { this.caminhoArquivo = caminhoArquivo; }
    public String getArtista() { return artista; }
    public void setArtista(String artista) {
        if (artista == null || artista.isBlank()) throw new IllegalArgumentException("Informe o artista.");
        this.artista = artista.trim();
    }
    @Override public String toString() {
        return id + " - " + titulo + " | " + artista + " | " + duracaoSegundos + "s";
    }
}