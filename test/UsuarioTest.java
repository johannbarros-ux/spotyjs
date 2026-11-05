import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class UsuarioTest {
    @Test void autenticaComSenhaCorretaERecusaSenhaErrada() {
        Ouvinte ouvinte = new Ouvinte(1, "Ana", "ana@example.com",
                PasswordUtils.gerarHash("senha123"), new Plano("Gratuito", false, 6));
        assertTrue(ouvinte.autenticar("ana@example.com", "senha123"));
        assertFalse(ouvinte.autenticar("ana@example.com", "outra-senha"));
    }

    @Test void usuarioBloqueadoNaoConsegueAutenticar() {
        Ouvinte ouvinte = new Ouvinte(1, "Ana", "ana@example.com",
                PasswordUtils.gerarHash("senha123"), new Plano("Gratuito", false, 6));
        ouvinte.setBloqueado(true);
        assertFalse(ouvinte.autenticar("ana@example.com", "senha123"));
    }
}