import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PasswordUtilsTest {
    @Test void hashValidaSenhaCorreta() {
        String hash = PasswordUtils.gerarHash("senha-forte");
        assertTrue(PasswordUtils.verificar("senha-forte", hash));
        assertFalse(PasswordUtils.verificar("senha-errada", hash));
    }
    @Test void rejeitaSenhaCurta() {
        assertThrows(IllegalArgumentException.class, () -> PasswordUtils.gerarHash("123"));
    }
}