package ar.edu.ub.club.accesos.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.edu.ub.club.accesos.aplicacion.ServicioAccesos;
import ar.edu.ub.club.accesos.aplicacion.SesionUsuario;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de {@code PresentadorInicioSesion} con una pantalla falsa (MVP: el presentador se prueba sin Vaadin) y
 * dobles de la fachada y del registro de sesión ({@code sec_cu11_p1.puml}).
 *
 * <p>Casos de uso: CU-11, CU-13
 */
class PresentadorInicioSesionTest {

    /** Pantalla falsa que registra lo que le pide el presentador. */
    static class PantallaFalsa implements PantallaInicioSesion {

        /** Último error mostrado. */
        String error;

        /** Última ruta pedida. */
        String ruta;

        /** Veces que se limpió la clave. */
        int limpiezas;

        /** Último estado pedido del botón Ingresar. */
        Boolean envioHabilitado;

        /**
         * Registra la limpieza.
         *
         * <p>Casos de uso: CU-11
         */
        @Override
        public void limpiarClave() {
            limpiezas++;
        }

        /**
         * Registra el estado del botón.
         *
         * <p>Casos de uso: CU-11
         * @param habilitado estado pedido
         */
        @Override
        public void habilitarEnvio(boolean habilitado) {
            envioHabilitado = habilitado;
        }

        /**
         * Registra el error.
         *
         * <p>Casos de uso: CU-11
         * @param mensaje mensaje
         */
        @Override
        public void mostrarError(String mensaje) {
            error = mensaje;
        }

        /**
         * No se usa en estas pruebas.
         *
         * <p>Casos de uso: CU-11
         * @param mensaje mensaje
         */
        @Override
        public void mostrarMensaje(String mensaje) {
            // sin efecto
        }

        /**
         * Registra el campo marcado como error.
         *
         * <p>Casos de uso: CU-11
         * @param campo campo
         * @param mensaje mensaje
         */
        @Override
        public void marcarCampo(String campo, String mensaje) {
            error = mensaje;
        }

        /**
         * Registra la navegación.
         *
         * <p>Casos de uso: CU-11, CU-13
         * @param ruta ruta
         */
        @Override
        public void navegarA(String ruta) {
            this.ruta = ruta;
        }
    }

    /** Doble de la fachada. */
    private ServicioAccesos servicio;

    /** Doble del registro de la sesión web. */
    private RegistroSesionWeb registro;

    /** Pantalla falsa. */
    private PantallaFalsa pantalla;

    /** Presentador bajo prueba. */
    private PresentadorInicioSesion presentador;

    /**
     * Arma el presentador con sus dobles.
     *
     * <p>Casos de uso: CU-11
     */
    @BeforeEach
    void preparar() {
        servicio = mock(ServicioAccesos.class);
        registro = mock(RegistroSesionWeb.class);
        pantalla = new PantallaFalsa();
        presentador = new PresentadorInicioSesion(servicio, registro);
        presentador.vincular(pantalla);
    }

    /**
     * Un administrador (con CONSULTAR_DASHBOARD) entra al panel después de registrar la sesión web.
     *
     * <p>Casos de uso: CU-11
     */
    @Test
    void administradorNavegaAlPanel() {
        SesionUsuario sesion = new SesionUsuario(1L, "Inicial, Administrador", Set.of(Permiso.CONSULTAR_DASHBOARD));
        when(servicio.iniciarSesion("admin", "clave", false)).thenReturn(sesion);
        presentador.alIngresar("admin", "clave", false);
        verify(registro).iniciar(sesion);
        assertEquals("panel", pantalla.ruta);
        assertEquals(1, pantalla.limpiezas);
    }

    /**
     * Un socio (sin CONSULTAR_DASHBOARD) entra a su inicio.
     *
     * <p>Casos de uso: CU-11
     */
    @Test
    void socioNavegaAlInicio() {
        SesionUsuario sesion = new SesionUsuario(10L, "Pérez, Juan", Set.of(Permiso.RESERVAR_ONLINE));
        when(servicio.iniciarSesion("juanperez", "clave", true)).thenReturn(sesion);
        presentador.alIngresar("juanperez", "clave", true);
        assertEquals("inicio", pantalla.ruta);
    }

    /**
     * Un rechazo de la fachada muestra el motivo, no registra la sesión y vuelve a habilitar el envío (FA4 a FA8).
     *
     * <p>Casos de uso: CU-11
     */
    @Test
    void rechazoMuestraElMotivo() {
        when(servicio.iniciarSesion(any(), any(), any(Boolean.class)))
                .thenThrow(new ErrorNegocio("Usuario o contraseña incorrectos."));
        presentador.alIngresar("admin", "mala", false);
        assertEquals("Usuario o contraseña incorrectos.", pantalla.error);
        assertNull(pantalla.ruta);
        assertTrue(pantalla.envioHabilitado);
        assertEquals(1, pantalla.limpiezas);
        verify(registro, never()).iniciar(any());
    }

    /**
     * «Olvidé mi contraseña» navega a P-02 (FA3, extensión hacia CU-13).
     *
     * <p>Casos de uso: CU-11, CU-13
     */
    @Test
    void olvideMiContrasenaNavegaARecuperar() {
        presentador.alOlvidarClave();
        assertEquals("recuperar-clave", pantalla.ruta);
    }
}
