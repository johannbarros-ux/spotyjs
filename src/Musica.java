public class Musica implements Reproduzivel {
    private final int id;
    private final String titulo;
    private final int duracaoSegundos;
    private final String formato;
    private boolean reproduzindo;

    public Musica(int id, String titulo, int duracaoSegundos, String formato) {
        if (duracaoSegundos <= 0) {
            throw new IllegalArgumentException("A duracao deve ser positiva");
        }
        this.id = id;
        this.titulo = titulo;
        this.duracaoSegundos = duracaoSegundos;
        this.formato = formato;
    }

    public int obterDuracao() {
        return duracaoSegundos;
    }

    public boolean validarFormato() {
        return formato != null
                && (formato.equalsIgnoreCase("mp3")
                || formato.equalsIgnoreCase("wav")
                || formato.equalsIgnoreCase("ogg"));
    }

    @Override
    public void reproduzir() {
        reproduzindo = true;
    }

    @Override
    public void pausar() {
        reproduzindo = false;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getFormato() {
        return formato;
    }

    public boolean isReproduzindo() {
        return reproduzindo;
    }
}
