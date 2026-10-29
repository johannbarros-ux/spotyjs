public class Usuario {
    private final int id;
    private String nome;
    private String email;
    private String senhaHash;
    private Plano plano;

    public Usuario(int id, String nome, String email, String senhaHash, Plano plano) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.plano = plano;
    }

    public boolean autenticar(String email, String senha) {
        return this.email.equals(email) && this.senhaHash.equals(senha);
    }

    public void seguir(Usuario usuario) {
        if (usuario == null || usuario == this) {
            return;
        }
        usuario.seguidores.add(this);
    }

    private final java.util.List<Usuario> seguidores = new java.util.ArrayList<>();

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public Plano getPlano() {
        return plano;
    }

    public void setPlano(Plano plano) {
        this.plano = plano;
    }

    public java.util.List<Usuario> getSeguidores() {
        return java.util.Collections.unmodifiableList(seguidores);
    }
}
