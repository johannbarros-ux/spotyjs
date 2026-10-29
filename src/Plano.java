public class Plano {
    private final String nome;
    private final boolean premium;
    private final int pulosPorHora;

    public Plano(String nome, boolean premium, int pulosPorHora) {
        if (pulosPorHora < 0) {
            throw new IllegalArgumentException("A quantidade de pulos nao pode ser negativa");
        }
        this.nome = nome;
        this.premium = premium;
        this.pulosPorHora = pulosPorHora;
    }

    public boolean permiteDownload() {
        return premium;
    }

    public boolean permitePulo() {
        return premium || pulosPorHora > 0;
    }

    public String getNome() {
        return nome;
    }

    public boolean isPremium() {
        return premium;
    }

    public int getPulosPorHora() {
        return pulosPorHora;
    }
}
