package ar.edu.ub.club.accesos.dominio;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de {@code PoliticaContrasenas} (RNF-03): longitud de 15 a 64 caracteres Unicode, lista prohibida y
 * distinta del nombre de usuario.
 *
 * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15
 */
class PoliticaContrasenasTest {

    /** Política con una lista prohibida chica. */
    private final PoliticaContrasenas politica =
            new PoliticaContrasenas(new ListaContrasenasProhibidas(Set.of("Contrasena123456")));

    /**
     * Una clave de 15 caracteres con espacios es válida (no se exigen tipos de caracteres).
     *
     * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15
     */
    @Test
    void aceptaQuinceCaracteresConEspacios() {
        assertDoesNotThrow(() -> politica.validar("mi perro bobby1", "juanperez"));
    }

    /**
     * Rechaza menos de 15 y más de 64 caracteres.
     *
     * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15
     */
    @Test
    void rechazaLongitudFueraDeRango() {
        assertThrows(ErrorNegocio.class, () -> politica.validar("catorce chars!", "juan"));
        assertThrows(ErrorNegocio.class, () -> politica.validar("x".repeat(65), "juan"));
        assertDoesNotThrow(() -> politica.validar("x".repeat(64), "juan"));
    }

    /**
     * Cuenta caracteres Unicode, no unidades UTF-16: 15 emojis son 15 caracteres y 40 no superan el máximo.
     *
     * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15
     */
    @Test
    void cuentaPuntosDeCodigoUnicode() {
        assertDoesNotThrow(() -> politica.validar("😀".repeat(15), "juan"));
        // 40 emojis son 80 unidades UTF-16 pero 40 caracteres: se aceptan
        assertDoesNotThrow(() -> politica.validar("😀".repeat(40), "juan"));
        assertThrows(ErrorNegocio.class, () -> politica.validar("😀".repeat(65), "juan"));
    }

    /**
     * Rechaza una clave de la lista prohibida sin distinguir mayúsculas.
     *
     * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15
     */
    @Test
    void rechazaListaProhibida() {
        assertThrows(ErrorNegocio.class, () -> politica.validar("CONTRASENA123456", "juan"));
    }

    /**
     * Rechaza una clave igual al nombre de usuario sin distinguir mayúsculas.
     *
     * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15
     */
    @Test
    void rechazaIgualAlUsuario() {
        assertThrows(ErrorNegocio.class, () -> politica.validar("AdministradorClub", "administradorclub"));
    }

    /**
     * La lista real del recurso de la aplicación se carga y contiene claves comunes.
     *
     * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15
     */
    @Test
    void cargaLaListaDelRecurso() {
        assertTrue(new ListaContrasenasProhibidas().contiene("PasswordPassword"));
    }
}
