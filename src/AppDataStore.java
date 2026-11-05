import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Persistencia local em JSON. O arquivo de dados fica fora de src/. */
public class AppDataStore {
    private static final Path ARQUIVO = Path.of("dados", "spotyjs.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void salvar(List<Usuario> usuarios, List<Musica> catalogo) throws IOException {
        Estado estado = new Estado();
        Map<Integer, Integer> donos = new HashMap<>();
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Cantor cantor)
                for (Musica musica : cantor.getMusicas()) donos.put(musica.getId(), cantor.getId());
        }
        for (Musica musica : catalogo) {
            MusicaRegistro m = new MusicaRegistro();
            m.id = musica.getId(); m.titulo = musica.getTitulo(); m.duracao = musica.obterDuracao();
            m.formato = musica.getFormato(); m.caminho = musica.getCaminhoArquivo(); m.artista = musica.getArtista();
            m.donoId = donos.getOrDefault(musica.getId(), 0);
            estado.musicas.add(m);
        }
        for (Usuario usuario : usuarios) {
            UsuarioRegistro u = new UsuarioRegistro();
            u.id = usuario.getId(); u.nome = usuario.getNome(); u.email = usuario.getEmail();
            u.senhaHash = usuario.getSenhaHash(); u.bloqueado = usuario.isBloqueado();
            Plano plano = usuario.getPlano();
            u.planoNome = plano == null ? "Gratuito" : plano.getNome();
            u.premium = plano != null && plano.isPremium();
            u.pulos = plano == null ? 6 : plano.getPulosPorHora();
            if (usuario instanceof Cantor cantor) { u.tipo = "CANTOR"; u.nomeArtistico = cantor.getNomeArtistico(); }
            else if (usuario instanceof Administrador admin) { u.tipo = "ADMIN"; u.nivelAcesso = admin.getNivelAcesso(); }
            else u.tipo = "OUVINTE";
            for (Usuario seguido : usuario.getSeguindo()) u.seguindo.add(seguido.getId());
            if (usuario instanceof Ouvinte ouvinte)
                for (Musica m : ouvinte.getCurtidas()) u.curtidas.add(m.getId());
            List<Playlist> playlists = usuario instanceof Ouvinte o ? o.getPlaylists()
                    : usuario instanceof Cantor c ? c.getPlaylists() : List.of();
            for (Playlist playlist : playlists) {
                PlaylistRegistro p = new PlaylistRegistro(); p.nome = playlist.getNome();
                for (Musica m : playlist.getMusicas()) p.musicas.add(m.getId());
                u.playlists.add(p);
            }
            estado.usuarios.add(u);
        }
        Files.createDirectories(ARQUIVO.getParent());
        Files.writeString(ARQUIVO, gson.toJson(estado), StandardCharsets.UTF_8);
    }

    public Dados carregar() throws IOException {
        if (!Files.exists(ARQUIVO)) return new Dados(new ArrayList<>(), new ArrayList<>());
        Estado estado = gson.fromJson(Files.readString(ARQUIVO, StandardCharsets.UTF_8), Estado.class);
        if (estado == null) return new Dados(new ArrayList<>(), new ArrayList<>());
        Map<Integer, Musica> musicas = new HashMap<>();
        List<Musica> catalogo = new ArrayList<>();
        for (MusicaRegistro r : estado.musicas) {
            Musica m = new Musica(r.id, r.titulo, r.duracao, r.formato, r.caminho, r.artista);
            musicas.put(m.getId(), m); catalogo.add(m);
        }
        Map<Integer, Usuario> usuarios = new HashMap<>();
        List<Usuario> listaUsuarios = new ArrayList<>();
        for (UsuarioRegistro r : estado.usuarios) {
            Plano plano = new Plano(r.planoNome == null ? "Gratuito" : r.planoNome, r.premium, r.pulos);
            Usuario u;
            if ("CANTOR".equals(r.tipo)) u = new Cantor(r.id, r.nome, r.email, r.senhaHash, plano, r.nomeArtistico);
            else if ("ADMIN".equals(r.tipo)) u = new Administrador(r.id, r.nome, r.email, r.senhaHash, plano, r.nivelAcesso);
            else u = new Ouvinte(r.id, r.nome, r.email, r.senhaHash, plano);
            u.setBloqueado(r.bloqueado); usuarios.put(u.getId(), u); listaUsuarios.add(u);
        }
        for (UsuarioRegistro r : estado.usuarios) {
            Usuario u = usuarios.get(r.id);
            for (Integer idSeguido : r.seguindo) if (usuarios.containsKey(idSeguido)) u.seguir(usuarios.get(idSeguido));
            if (u instanceof Ouvinte ouvinte)
                for (Integer idMusica : r.curtidas) if (musicas.containsKey(idMusica)) ouvinte.curtir(musicas.get(idMusica));
            for (PlaylistRegistro pr : r.playlists) {
                Playlist playlist = u instanceof Ouvinte o ? o.criarPlaylist(pr.nome)
                        : u instanceof Cantor c ? c.criarPlaylist(pr.nome) : null;
                if (playlist != null) for (Integer idMusica : pr.musicas)
                    if (musicas.containsKey(idMusica)) playlist.adicionarMusica(musicas.get(idMusica));
            }
        }
        for (MusicaRegistro r : estado.musicas)
            if (r.donoId != 0 && usuarios.get(r.donoId) instanceof Cantor cantor && musicas.containsKey(r.id))
                cantor.enviarMusica(musicas.get(r.id));
        return new Dados(listaUsuarios, catalogo);
    }

    public static class Dados {
        public final List<Usuario> usuarios;
        public final List<Musica> catalogo;
        Dados(List<Usuario> usuarios, List<Musica> catalogo) { this.usuarios = usuarios; this.catalogo = catalogo; }
    }
    private static class Estado { List<UsuarioRegistro> usuarios = new ArrayList<>(); List<MusicaRegistro> musicas = new ArrayList<>(); }
    private static class UsuarioRegistro {
        int id; String tipo; String nome; String email; String senhaHash; String nomeArtistico; String nivelAcesso;
        String planoNome; boolean premium; int pulos; boolean bloqueado;
        List<Integer> seguindo = new ArrayList<>(); List<Integer> curtidas = new ArrayList<>();
        List<PlaylistRegistro> playlists = new ArrayList<>();
    }
    private static class MusicaRegistro {
        int id; String titulo; int duracao; String formato; String caminho; String artista; int donoId;
    }
    private static class PlaylistRegistro { String nome; List<Integer> musicas = new ArrayList<>(); }
}