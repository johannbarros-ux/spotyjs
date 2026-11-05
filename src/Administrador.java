import java.util.Collection;

public class Administrador extends Usuario {
    private final String nivelAcesso;
    public Administrador(int id, String nome, String email, String senhaHash, Plano plano, String nivelAcesso) {
        super(id, nome, email, senhaHash, plano);
        this.nivelAcesso = nivelAcesso == null ? "padrao" : nivelAcesso;
    }
    public boolean removerMusica(Musica musica, Collection<Musica> catalogo) {
        return musica != null && catalogo != null && catalogo.remove(musica);
    }
    public void bloquearUsuario(Usuario usuario) {
        if (usuario != null && !(usuario instanceof Administrador)) usuario.setBloqueado(true);
    }
    public void desbloquearUsuario(Usuario usuario) {
        if (usuario != null) usuario.setBloqueado(false);
    }
    public String getNivelAcesso() { return nivelAcesso; }
}