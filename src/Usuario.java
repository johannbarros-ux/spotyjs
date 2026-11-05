import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Usuario {
    private final int id;
    private String nome;
    private final String email;
    private final String senhaHash;
    private Plano plano;
    private boolean bloqueado;
    private final List<Usuario> seguidores = new ArrayList<>();
    private final List<Usuario> seguindo = new ArrayList<>();

    protected Usuario(int id, String nome, String email, String senhaHash, Plano plano) {
        if (id <= 0) throw new IllegalArgumentException("ID invalido.");
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Informe o nome.");
        if (email == null || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) throw new IllegalArgumentException("E-mail invalido.");
        if (senhaHash == null || senhaHash.isBlank()) throw new IllegalArgumentException("Senha invalida.");
        this.id = id;
        this.nome = nome.trim();
        this.email = email.trim().toLowerCase();
        this.senhaHash = senhaHash;
        this.plano = plano;
    }

    public boolean autenticar(String email, String senha) {
        return !bloqueado && this.email.equalsIgnoreCase(email == null ? "" : email.trim())
                && PasswordUtils.verificar(senha, senhaHash);
    }

    public void seguir(Usuario usuario) {
        if (usuario == null || usuario == this || bloqueado || usuario.bloqueado || seguindo.contains(usuario)) return;
        seguindo.add(usuario);
        usuario.seguidores.add(this);
    }

    public void deixarDeSeguir(Usuario usuario) {
        if (usuario == null) return;
        seguindo.remove(usuario);
        usuario.seguidores.remove(this);
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome nao pode ficar vazio.");
        this.nome = nome.trim();
    }
    public String getEmail() { return email; }
    public String getSenhaHash() { return senhaHash; }
    public Plano getPlano() { return plano; }
    public void setPlano(Plano plano) { this.plano = plano; }
    public boolean isBloqueado() { return bloqueado; }
    public void setBloqueado(boolean bloqueado) { this.bloqueado = bloqueado; }
    public List<Usuario> getSeguidores() { return Collections.unmodifiableList(seguidores); }
    public List<Usuario> getSeguindo() { return Collections.unmodifiableList(seguindo); }
}