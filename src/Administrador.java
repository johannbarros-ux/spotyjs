public class Administrador extends Usuario {
    private final String nivelAcesso;

    public Administrador(int id, String nome, String email, String senhaHash, Plano plano, String nivelAcesso) {
        super(id, nome, email, senhaHash, plano);
        this.nivelAcesso = nivelAcesso;
    }

    public void removerMusica(Musica musica, java.util.Collection<Musica> catalogo) {
        if (catalogo != null) {
            catalogo.remove(musica);
        }
    }

    public void bloquearUsuario(Usuario usuario) {
        if (usuario != null) {
            usuario.setPlano(null);
        }
    }

    public String getNivelAcesso() {
        return nivelAcesso;
    }
}
