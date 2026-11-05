import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AppDataStore armazenamento = new AppDataStore();
    private static final Player player = new Player();
    private static final FilaDeReproducao fila = new FilaDeReproducao();
    private static final List<Usuario> usuarios = new ArrayList<>();
    private static final List<Musica> catalogo = new ArrayList<>();
    private static int proximoIdUsuario = 1;
    private static int proximoIdMusica = 1;

    public static void main(String[] args) {
        carregarDados();
        if (usuarios.stream().noneMatch(Administrador.class::isInstance)) criarAdminInicial();
        carregarCatalogoInicial();
        System.out.println("\n=== SPOTYJS ===");
        boolean executando = true;
        while (executando) {
            System.out.println("\n1 - Entrar\n2 - Criar conta\n0 - Sair");
            switch (lerInteiro("Opcao: ")) {
                case 1 -> entrar();
                case 2 -> cadastrar();
                case 0 -> executando = false;
                default -> System.out.println("Opcao invalida.");
            }
        }
        salvarDados();
        player.parar();
        System.out.println("Dados salvos. Ate mais!");
    }

    private static void carregarDados() {
        try {
            AppDataStore.Dados dados = armazenamento.carregar();
            usuarios.addAll(dados.usuarios); catalogo.addAll(dados.catalogo);
            for (Usuario u : usuarios) proximoIdUsuario = Math.max(proximoIdUsuario, u.getId() + 1);
            for (Musica m : catalogo) proximoIdMusica = Math.max(proximoIdMusica, m.getId() + 1);
        } catch (Exception ex) {
            System.out.println("Nao foi possivel carregar os dados: " + ex.getMessage());
        }
    }

    private static void criarAdminInicial() {
        String senha = System.getenv().getOrDefault("SPOTYJS_ADMIN_PASSWORD", "admin123");
        usuarios.add(new Administrador(proximoIdUsuario++, "Administrador", "admin@spotyjs.com",
                PasswordUtils.gerarHash(senha), new Plano("Administrativo", true, 999), "total"));
        System.out.println("Conta administrativa inicial: admin@spotyjs.com");
        if (System.getenv("SPOTYJS_ADMIN_PASSWORD") == null)
            System.out.println("Senha inicial: admin123 (altere SPOTYJS_ADMIN_PASSWORD antes de usar publicamente).");
    }

    private static void carregarCatalogoInicial() {
        adicionarFaixaSeExistir("Garota de Ipanema", "Antônio Carlos Jobim", "src/musicas/Garota de Ipanema - Antônio Carlos Jobim.mp3", 180);
        adicionarFaixaSeExistir("Gnarly", "KATSEYE", "src/musicas/Gnarly - KATSEYE.mp3", 170);
        adicionarFaixaSeExistir("Lover Girl", "Laufey", "src/musicas/Lover Girl - Laufey.mp3", 210);
    }
    private static void adicionarFaixaSeExistir(String titulo, String artista, String caminho, int duracao) {
        boolean existe = catalogo.stream().anyMatch(m -> m.getCaminhoArquivo() != null && m.getCaminhoArquivo().equals(caminho));
        if (!existe && Files.isRegularFile(Path.of(caminho)))
            catalogo.add(new Musica(proximoIdMusica++, titulo, duracao, "mp3", caminho, artista));
    }

    private static void cadastrar() {
        System.out.println("\nTipo de conta: 1 - Ouvinte | 2 - Cantor");
        int tipo = lerInteiro("Escolha: ");
        if (tipo != 1 && tipo != 2) { System.out.println("Tipo invalido."); return; }
        String nome = lerTexto("Nome: ");
        String email = lerTexto("E-mail: ").toLowerCase();
        if (usuarios.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email))) {
            System.out.println("Ja existe uma conta com esse e-mail."); return;
        }
        String senha = lerTexto("Senha (minimo 6 caracteres): ");
        try {
            String hash = PasswordUtils.gerarHash(senha);
            Plano plano = new Plano("Gratuito", false, 6);
            Usuario novo;
            if (tipo == 2) novo = new Cantor(proximoIdUsuario++, nome, email, hash, plano, lerTexto("Nome artistico: "));
            else novo = new Ouvinte(proximoIdUsuario++, nome, email, hash, plano);
            usuarios.add(novo); salvarDados();
            System.out.println("Conta criada com sucesso!");
            menuUsuario(novo);
        } catch (IllegalArgumentException ex) { System.out.println(ex.getMessage()); }
    }

    private static void entrar() {
        String email = lerTexto("E-mail: ");
        String senha = lerTexto("Senha: ");
        for (Usuario usuario : usuarios) {
            if (usuario.autenticar(email, senha)) { menuUsuario(usuario); return; }
        }
        System.out.println("E-mail ou senha incorretos, ou conta bloqueada.");
    }

    private static void menuUsuario(Usuario usuario) {
        if (usuario instanceof Administrador admin) menuAdministrador(admin);
        else if (usuario instanceof Cantor cantor) menuCantor(cantor);
        else if (usuario instanceof Ouvinte ouvinte) menuOuvinte(ouvinte);
    }

    private static void menuOuvinte(Ouvinte ouvinte) {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- OUVINTE: " + ouvinte.getNome() + " ---");
            System.out.println("1 Catalogo | 2 Reproduzir musica | 3 Curtidas | 4 Criar playlist | 5 Ver playlists | 6 Seguir cantor | 7 Player (pausar/retomar/parar) | 0 Sair");
            switch (lerInteiro("Opcao: ")) {
                case 1 -> listarCatalogo();
                case 2 -> reproduzirSelecionada();
                case 3 -> gerenciarCurtidas(ouvinte);
                case 4 -> criarPlaylist(ouvinte);
                case 5 -> listarPlaylists(ouvinte);
                case 6 -> seguirCantor(ouvinte);
                case 7 -> controlarPlayer();
                case 0 -> voltar = true;
                default -> System.out.println("Opcao invalida.");
            }
        }
        salvarDados();
    }

    private static void menuCantor(Cantor cantor) {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- CANTOR: " + cantor.getNomeArtistico() + " ---");
            System.out.println("1 Catalogo | 2 Cadastrar musica | 3 Minhas musicas | 4 Seguidores | 5 Criar playlist | 0 Sair");
            switch (lerInteiro("Opcao: ")) {
                case 1 -> listarCatalogo();
                case 2 -> cadastrarMusica(cantor);
                case 3 -> cantor.getMusicas().forEach(System.out::println);
                case 4 -> System.out.println("Seguidores: " + cantor.getSeguidores().size());
                case 5 -> { cantor.criarPlaylist(lerTexto("Nome da playlist: ")); salvarDados(); }
                case 0 -> voltar = true;
                default -> System.out.println("Opcao invalida.");
            }
        }
        salvarDados();
    }

    private static void menuAdministrador(Administrador admin) {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- ADMINISTRADOR ---");
            System.out.println("1 Catalogo | 2 Usuarios | 3 Bloquear/desbloquear usuario | 4 Remover musica | 0 Sair");
            switch (lerInteiro("Opcao: ")) {
                case 1 -> listarCatalogo();
                case 2 -> usuarios.forEach(u -> System.out.println(u.getId() + " - " + u.getNome() + " (" + u.getEmail() + ") " + (u.isBloqueado() ? "[BLOQUEADO]" : "")));
                case 3 -> alterarBloqueio(admin);
                case 4 -> removerMusica(admin);
                case 0 -> voltar = true;
                default -> System.out.println("Opcao invalida.");
            }
        }
        salvarDados();
    }

    private static void listarCatalogo() {
        if (catalogo.isEmpty()) { System.out.println("Catalogo vazio."); return; }
        catalogo.forEach(System.out::println);
    }
    private static void reproduzirSelecionada() {
        listarCatalogo();
        int id = lerInteiro("ID da musica: ");
        Musica musica = buscarMusica(id);
        if (musica == null) { System.out.println("Musica nao encontrada."); return; }
        fila.adicionar(musica); player.reproduzir(musica);
    }
    private static void controlarPlayer() {
        System.out.println("1 Pausar | 2 Retomar | 3 Parar | 4 Proxima | 5 Anterior");
        switch (lerInteiro("Opcao: ")) {
            case 1 -> player.pausar();
            case 2 -> player.reproduzir();
            case 3 -> player.parar();
            case 4 -> player.avancar(fila);
            case 5 -> player.retroceder(fila);
            default -> System.out.println("Opcao invalida.");
        }
    }
    private static void gerenciarCurtidas(Ouvinte ouvinte) {
        System.out.println("1 Curtir | 2 Ver curtidas | 3 Remover curtida");
        int op = lerInteiro("Opcao: ");
        if (op == 2) { ouvinte.getCurtidas().forEach(System.out::println); return; }
        listarCatalogo();
        Musica m = buscarMusica(lerInteiro("ID da musica: "));
        if (m == null) { System.out.println("Musica nao encontrada."); return; }
        if (op == 1) ouvinte.curtir(m); else if (op == 3) ouvinte.descurtir(m);
        salvarDados();
    }
    private static void criarPlaylist(Ouvinte ouvinte) {
        Playlist p = ouvinte.criarPlaylist(lerTexto("Nome da playlist: "));
        boolean adicionando = true;
        while (adicionando) {
            listarCatalogo();
            int id = lerInteiro("ID da musica (0 para terminar): ");
            if (id == 0) break;
            Musica m = buscarMusica(id);
            if (m != null) p.adicionarMusica(m); else System.out.println("Musica nao encontrada.");
        }
        salvarDados();
    }
    private static void listarPlaylists(Usuario usuario) {
        List<Playlist> playlists = usuario instanceof Ouvinte o ? o.getPlaylists() : ((Cantor) usuario).getPlaylists();
        for (Playlist p : playlists) {
            System.out.println("\n" + p.getNome());
            p.getMusicas().forEach(System.out::println);
        }
    }
    private static void seguirCantor(Ouvinte ouvinte) {
        for (Usuario u : usuarios) if (u instanceof Cantor) System.out.println(u.getId() + " - " + u.getNome() + " / " + ((Cantor) u).getNomeArtistico());
        int id = lerInteiro("ID do cantor: ");
        Usuario escolhido = buscarUsuario(id);
        if (escolhido instanceof Cantor) { ouvinte.seguir(escolhido); salvarDados(); System.out.println("Agora voce segue " + escolhido.getNome()); }
        else System.out.println("Cantor nao encontrado.");
    }
    private static void cadastrarMusica(Cantor cantor) {
        String titulo = lerTexto("Titulo: ");
        String caminho = lerTexto("Caminho do arquivo MP3: ");
        Path arquivo = Path.of(caminho);
        if (!Files.isRegularFile(arquivo) || !arquivo.toString().toLowerCase().endsWith(".mp3")) {
            System.out.println("Informe o caminho de um arquivo MP3 existente."); return;
        }
        int duracao = lerInteiro("Duracao em segundos: ");
        try {
            Musica m = new Musica(proximoIdMusica++, titulo, duracao, "mp3", arquivo.toString(), cantor.getNomeArtistico());
            cantor.enviarMusica(m); catalogo.add(m); salvarDados();
            System.out.println("Musica adicionada ao catalogo!");
        } catch (IllegalArgumentException ex) { System.out.println(ex.getMessage()); }
    }
    private static void alterarBloqueio(Administrador admin) {
        usuarios.forEach(u -> System.out.println(u.getId() + " - " + u.getEmail() + (u.isBloqueado() ? " [BLOQUEADO]" : "")));
        Usuario u = buscarUsuario(lerInteiro("ID do usuario: "));
        if (u == null || u == admin || u instanceof Administrador) { System.out.println("Usuario invalido."); return; }
        if (u.isBloqueado()) admin.desbloquearUsuario(u); else admin.bloquearUsuario(u);
        salvarDados();
    }
    private static void removerMusica(Administrador admin) {
        listarCatalogo();
        Musica m = buscarMusica(lerInteiro("ID da musica a remover: "));
        if (admin.removerMusica(m, catalogo)) { salvarDados(); System.out.println("Musica removida."); }
        else System.out.println("Musica nao encontrada.");
    }
    private static Musica buscarMusica(int id) { return catalogo.stream().filter(m -> m.getId() == id).findFirst().orElse(null); }
    private static Usuario buscarUsuario(int id) { return usuarios.stream().filter(u -> u.getId() == id).findFirst().orElse(null); }
    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try { return Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException ex) { System.out.println("Digite um numero valido."); }
        }
    }
    private static String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim();
            if (!valor.isEmpty()) return valor;
            System.out.println("O campo nao pode ficar vazio.");
        }
    }
    private static void salvarDados() {
        try { armazenamento.salvar(usuarios, catalogo); }
        catch (IOException ex) { System.err.println("Erro ao salvar dados: " + ex.getMessage()); }
    }
}