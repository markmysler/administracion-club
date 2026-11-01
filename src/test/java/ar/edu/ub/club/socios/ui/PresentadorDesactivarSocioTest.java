package ar.edu.ub.club.socios.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ar.edu.ub.club.compartido.dominio.ResultadoBaja;
import ar.edu.ub.club.compartido.dominio.ResumenReserva;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Prueba del presentador del diálogo P-09 con una {@code PantallaDesactivarSocio} falsa (MVP Vista Pasiva: el
 * presentador se prueba sin Vaadin) y la fachada como doble de Mockito.
 *
 * <p>Casos de uso: CU-03
 */
class PresentadorDesactivarSocioTest {

    /**
     * Pantalla falsa que registra lo que el presentador le pide mostrar.
     *
     * <p>Casos de uso: CU-03
     */
    private static final class PantallaFalsa implements PantallaDesactivarSocio {

        /** Reservas mostradas ({@code null} si no se llamó). */
        private List<FilaResumenReserva> afectadas;

        /** Errores mostrados. */
        private final List<String> errores = new ArrayList<>();

        /** Si se cerró con éxito. */
        private boolean cerrado;

        /**
         * Registra el socio (sin uso en las aserciones).
         *
         * <p>Casos de uso: CU-03
         * @param nombre nombre
         * @param dni documento
         */
        @Override
        public void mostrarSocio(String nombre, String dni) {
        }

        /**
         * Registra las reservas afectadas.
         *
         * <p>Casos de uso: CU-03
         * @param reservas reservas
         * @param candidatosTitular candidatos
         */
        @Override
        public void mostrarAfectadas(List<FilaResumenReserva> reservas, List<OpcionSeleccion> candidatosTitular) {
            this.afectadas = reservas;
        }

        /**
         * Registra el cierre con éxito.
         *
         * <p>Casos de uso: CU-03
         */
        @Override
        public void cerrarConExito() {
            cerrado = true;
        }

        /**
         * Registra un error.
         *
         * <p>Casos de uso: CU-03
         * @param mensaje mensaje
         */
        @Override
        public void mostrarError(String mensaje) {
            errores.add(mensaje);
        }

        /**
         * Ignora los mensajes informativos.
         *
         * <p>Casos de uso: CU-03
         * @param mensaje mensaje
         */
        @Override
        public void mostrarMensaje(String mensaje) {
        }

        /**
         * Registra un error de campo como error.
         *
         * <p>Casos de uso: CU-03
         * @param campo campo
         * @param mensaje mensaje
         */
        @Override
        public void marcarCampo(String campo, String mensaje) {
            errores.add(mensaje);
        }

        /**
         * Ignora la navegación.
         *
         * <p>Casos de uso: CU-03
         * @param ruta ruta
         */
        @Override
        public void navegarA(String ruta) {
        }
    }

    /**
     * Al abrir muestra las reservas afectadas; al confirmar con un error queda abierto; al confirmar bien cierra.
     *
     * <p>Casos de uso: CU-03
     */
    @Test
    void flujoDeDosPasos() {
        ServicioSocios servicio = mock(ServicioSocios.class);
        List<ResumenReserva> reservas = List.of(new ResumenReserva(5L, "Pérez, Ana", "Cancha 2",
                LocalDate.of(2026, 12, 3), LocalTime.of(18, 0), LocalTime.of(19, 0)));
        when(servicio.desactivarSocio(1L, null, false)).thenReturn(ResultadoBaja.pendiente(reservas));
        when(servicio.desactivarSocio(1L, null, true)).thenThrow(new ErrorNegocio("idNuevoTitular", "Falta titular"));
        when(servicio.desactivarSocio(1L, 2L, true)).thenReturn(ResultadoBaja.realizada(reservas));
        PresentadorDesactivarSocio presentador = new PresentadorDesactivarSocio(servicio);
        PantallaFalsa pantalla = new PantallaFalsa();
        presentador.vincular(pantalla);

        presentador.alAbrir(1L, "Pérez, Ana", "30111222", List.of(new OpcionSeleccion(2L, "Pérez, Beto")));
        assertEquals(1, pantalla.afectadas.size());
        assertEquals(5L, pantalla.afectadas.get(0).idReserva());
        assertFalse(pantalla.cerrado);

        presentador.alConfirmar(null);
        assertEquals(List.of("Falta titular"), pantalla.errores);
        assertFalse(pantalla.cerrado);

        presentador.alConfirmar(2L);
        assertTrue(pantalla.cerrado);
    }
}
