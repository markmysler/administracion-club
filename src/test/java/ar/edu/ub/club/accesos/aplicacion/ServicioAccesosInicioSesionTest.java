package ar.edu.ub.club.accesos.aplicacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.edu.ub.club.accesos.dominio.Administrador;
import ar.edu.ub.club.accesos.dominio.CodificadorClave;
import ar.edu.ub.club.accesos.dominio.ListaContrasenasProhibidas;
import ar.edu.ub.club.accesos.dominio.PoliticaContrasenas;
import ar.edu.ub.club.accesos.persistencia.UsuarioPorUsername;
import ar.edu.ub.club.auditoria.aplicacion.ServicioAuditoria;
import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.notificaciones.integracion.ServicioCorreo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de {@code ServicioAccesos.iniciarSesion} sin base de datos ({@code sec_cu11_p2.puml},
 * {@code sec_cu11_p3.puml}): credenciales inválidas, bloqueo al quinto intento, cuenta inactiva e ingreso exitoso
 * sin token persistente para un administrador (FA9).
 *
 * <p>Casos de uso: CU-11
 */
class ServicioAccesosInicioSesionTest {

    /** Momento fijo. */
    private static final LocalDateTime AHORA = LocalDateTime.of(2026, 11, 2, 10, 0);

    /** Doble de la fachada de persistencia. */
    private FachadaPersistencia fachada;

    /** Doble del gestor de sesiones. */
    private GestorSesiones gestor;

    /** Cuenta de prueba (clave "clave correcta"). */
    private Administrador admin;

    /** Fachada bajo prueba. */
    private ServicioAccesos servicio;

    /**
     * Arma la fachada con dobles y un codificador trivial.
     *
     * <p>Casos de uso: CU-11
     */
    @BeforeEach
    void preparar() {
        fachada = mock(FachadaPersistencia.class);
        gestor = mock(GestorSesiones.class);
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
        Reloj reloj = new Reloj() {
            @Override
            public LocalDate hoy() {
                return AHORA.toLocalDate();
            }

            @Override
            public LocalDateTime ahora() {
                return AHORA;
            }
        };
        admin = Administrador.reconstituir(1L, "admin", "admin@club.local", "h:clave correcta",
                ar.edu.ub.club.accesos.dominio.EstadoCuenta.ACTIVA, 0, null, AHORA.minusDays(10), "Admin", "Inicial");
        servicio = new ServicioAccesos(fachada, codificador, mock(ServicioCorreo.class), new ControlAcceso(),
                mock(UsuarioActual.class), mock(ServicioAuditoria.class), gestor,
                new PoliticaContrasenas(new ListaContrasenasProhibidas(Set.of())), reloj, "http://localhost");
        when(fachada.buscarUno(any(UsuarioPorUsername.class))).thenReturn(Optional.of(admin));
    }

    /**
     * Usuario inexistente: mensaje genérico (FA4).
     *
     * <p>Casos de uso: CU-11
     */
    @Test
    void usuarioInexistente() {
        when(fachada.buscarUno(any(UsuarioPorUsername.class))).thenReturn(Optional.empty());
        ErrorNegocio e = assertThrows(ErrorNegocio.class, () -> servicio.iniciarSesion("nadie", "x", false));
        assertEquals("Usuario o contraseña incorrectos.", e.getMensaje());
    }

    /**
     * Cinco claves incorrectas bloquean la cuenta y el intento queda guardado ({@code put}) en cada rechazo.
     *
     * <p>Casos de uso: CU-11
     */
    @Test
    void quintoFalloBloquea() {
        for (int i = 0; i < 4; i++) {
            assertThrows(ErrorNegocio.class, () -> servicio.iniciarSesion("admin", "mala", false));
        }
        ErrorNegocio e = assertThrows(ErrorNegocio.class, () -> servicio.iniciarSesion("admin", "mala", false));
        assertTrue(e.getMensaje().contains("bloqueada"));
        verify(fachada, times(5)).put(admin);
        ErrorNegocio bloqueada = assertThrows(ErrorNegocio.class,
                () -> servicio.iniciarSesion("admin", "clave correcta", false));
        assertTrue(bloqueada.getMensaje().contains("15 minutos"));
    }

    /**
     * Cuenta inactiva con clave correcta: no habilitada (FA8).
     *
     * <p>Casos de uso: CU-11
     */
    @Test
    void cuentaInactiva() {
        admin.desactivar();
        ErrorNegocio e = assertThrows(ErrorNegocio.class,
                () -> servicio.iniciarSesion("admin", "clave correcta", false));
        assertTrue(e.getMensaje().contains("no está habilitada"));
    }

    /**
     * Ingreso correcto de un administrador con la casilla marcada: sesión con sus permisos y sin token (FA9).
     *
     * <p>Casos de uso: CU-11
     */
    @Test
    void ingresoCorrectoDeAdministradorSinTokenPersistente() {
        SesionUsuario sesion = servicio.iniciarSesion("admin", "clave correcta", true);
        assertEquals(1L, sesion.getIdUsuario());
        assertEquals(admin.obtenerPermisos(), sesion.getPermisos());
        assertNull(sesion.getTokenPersistente());
        verify(gestor, never()).emitirTokenPersistente(any());
        assertEquals(0, admin.getIntentosFallidos());
    }
}
