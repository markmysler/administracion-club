package ar.edu.ub.club.accesos.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de las transiciones de una cuenta ({@code est_cuenta.puml}): Activa ⇄ Bloqueada temporalmente
 * (PoliticaBloqueo: 5 intentos, 15 minutos) y Activa → Inactiva (final); y del token de recuperación de un solo
 * uso.
 *
 * <p>Casos de uso: CU-11, CU-13, CU-17
 */
class TransicionesCuentaTest {

    /** Momento fijo de la prueba. */
    private static final LocalDateTime AHORA = LocalDateTime.of(2026, 11, 2, 10, 0);

    /** Cuenta bajo prueba. */
    private Administrador cuenta;

    /**
     * Crea una cuenta activa nueva.
     *
     * <p>Casos de uso: CU-11
     */
    @BeforeEach
    void crearCuenta() {
        cuenta = new Administrador("Laura", "Gómez", "laura", "laura@club.local", "hash");
    }

    /**
     * Una cuenta nueva nace activa, sin intentos y sin bloqueo (CU-16).
     *
     * <p>Casos de uso: CU-14, CU-16
     */
    @Test
    void cuentaNuevaNaceActivaSinIntentos() {
        assertTrue(cuenta.estaActiva());
        assertEquals(0, cuenta.getIntentosFallidos());
        assertNull(cuenta.getBloqueadoHasta());
        assertFalse(cuenta.estaBloqueada(AHORA));
    }

    /**
     * Cuatro intentos fallidos no bloquean; el quinto bloquea 15 minutos (RN-02, CU-11 FA7).
     *
     * <p>Casos de uso: CU-11
     */
    @Test
    void quintoIntentoFallidoBloqueaQuinceMinutos() {
        for (int i = 0; i < 4; i++) {
            cuenta.registrarIntentoFallido(AHORA);
        }
        assertFalse(cuenta.estaBloqueada(AHORA));
        cuenta.registrarIntentoFallido(AHORA);
        assertTrue(cuenta.estaBloqueada(AHORA));
        assertEquals(AHORA.plusMinutes(15), cuenta.getBloqueadoHasta());
        assertTrue(cuenta.estaBloqueada(AHORA.plusMinutes(14)));
    }

    /**
     * Al vencer los 15 minutos la cuenta se desbloquea sola; un nuevo fallo recomienza la serie (no supera 5).
     *
     * <p>Casos de uso: CU-11
     */
    @Test
    void bloqueoVenceYElContadorRecomienza() {
        for (int i = 0; i < 5; i++) {
            cuenta.registrarIntentoFallido(AHORA);
        }
        LocalDateTime despues = AHORA.plusMinutes(15);
        assertFalse(cuenta.estaBloqueada(despues));
        cuenta.registrarIntentoFallido(despues);
        assertEquals(1, cuenta.getIntentosFallidos());
        assertFalse(cuenta.estaBloqueada(despues));
    }

    /**
     * Un ingreso exitoso reinicia el contador y borra el bloqueo (CU-11 paso 8).
     *
     * <p>Casos de uso: CU-11, CU-13
     */
    @Test
    void reiniciarIntentosDesbloquea() {
        for (int i = 0; i < 5; i++) {
            cuenta.registrarIntentoFallido(AHORA);
        }
        cuenta.reiniciarIntentos();
        assertEquals(0, cuenta.getIntentosFallidos());
        assertFalse(cuenta.estaBloqueada(AHORA));
    }

    /**
     * Desactivar lleva la cuenta a INACTIVA (estado final).
     *
     * <p>Casos de uso: CU-17
     */
    @Test
    void desactivarPasaAInactiva() {
        cuenta.desactivar();
        assertFalse(cuenta.estaActiva());
        assertEquals(EstadoCuenta.INACTIVA, cuenta.getEstado());
    }

    /**
     * Autenticar delega en el codificador (la cuenta no conoce BCrypt).
     *
     * <p>Casos de uso: CU-11
     */
    @Test
    void autenticarDelegaEnElCodificador() {
        CodificadorClave codificador = new CodificadorClave() {
            @Override
            public String codificar(String clave) {
                return "h:" + clave;
            }

            @Override
            public boolean coincide(String clave, String hash) {
                return ("h:" + clave).equals(hash);
            }
        };
        Administrador a = new Administrador("A", "B", "abcd", "a@b.c", codificador.codificar("una clave larga de prueba"));
        assertTrue(a.autenticar("una clave larga de prueba", codificador));
        assertFalse(a.autenticar("otra clave", codificador));
    }

    /**
     * El token de recuperación es válido una sola vez y hasta su vencimiento (CU-13 FA4).
     *
     * <p>Casos de uso: CU-13
     */
    @Test
    void tokenRecuperacionDeUnSoloUso() {
        TokenRecuperacion t = new TokenRecuperacion(cuenta, "hash", AHORA.plusMinutes(30));
        assertTrue(t.esValido(AHORA));
        assertFalse(t.esValido(AHORA.plusMinutes(30)));
        t.marcarUsado();
        assertFalse(t.esValido(AHORA));
    }

    /**
     * El token persistente nuevo no está persistido; rotar extiende el vencimiento 30 días.
     *
     * <p>Casos de uso: CU-11
     */
    @Test
    void tokenPersistenteRotaYExtiendeVencimiento() {
        TokenSesionPersistente t = new TokenSesionPersistente("serie", cuenta, "h1", AHORA);
        assertTrue(t.esNuevo());
        t.marcarPersistido();
        assertFalse(t.esNuevo());
        t.rotar("h2", AHORA.plusDays(3));
        assertEquals("h2", t.getTokenHash());
        assertEquals(AHORA.plusDays(33), t.getExpiraEn());
    }
}
