import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Utilitarios para armazenar senhas com PBKDF2, salt individual e comparacao segura. */
public final class PasswordUtils {
    private static final int ITERACOES = 120_000;
    private static final int TAMANHO_CHAVE = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtils() {}

    public static String gerarHash(String senha) {
        if (senha == null || senha.length() < 6) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres.");
        }
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        byte[] hash = derivar(senha.toCharArray(), salt);
        return ITERACOES + ":" + Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verificar(String senha, String armazenada) {
        if (senha == null || armazenada == null) return false;
        try {
            String[] partes = armazenada.split(":");
            if (partes.length != 3) return false;
            int iteracoes = Integer.parseInt(partes[0]);
            byte[] salt = Base64.getDecoder().decode(partes[1]);
            byte[] esperado = Base64.getDecoder().decode(partes[2]);
            byte[] obtido = derivar(senha.toCharArray(), salt, iteracoes);
            return MessageDigest.isEqual(esperado, obtido);
        } catch (RuntimeException ex) {
            return false;
        }
    }

    private static byte[] derivar(char[] senha, byte[] salt) {
        return derivar(senha, salt, ITERACOES);
    }

    private static byte[] derivar(char[] senha, byte[] salt, int iteracoes) {
        try {
            PBEKeySpec spec = new PBEKeySpec(senha, salt, iteracoes, TAMANHO_CHAVE);
            try {
                return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
            } finally {
                spec.clearPassword();
                java.util.Arrays.fill(senha, '\0');
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Nao foi possivel proteger a senha.", ex);
        }
    }
}