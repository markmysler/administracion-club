package ar.edu.ub.club.accesos.aplicacion;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.edu.ub.club.compartido.dominio.Reloj;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

/**
 * Prueba de la tarea programada de limpieza de tokens de sesión persistente vencidos.
 *
 * <p>Casos de uso: CU-11; tareas programadas: limpieza de tokens de sesión persistente vencidos
 */
class PlanificadorSesionesTest {

    /**
     * La tarea pide al almacén borrar los tokens vencidos al momento actual del reloj del club.
     *
     * <p>Casos de uso: CU-11; tareas programadas: limpieza de tokens de sesión persistente vencidos
     */
    @Test
    void eliminaLosTokensVencidosAlMomentoActual() {
        AlmacenTokensSesionPersistente almacen = mock(AlmacenTokensSesionPersistente.class);
        Reloj reloj = mock(Reloj.class);
        LocalDateTime ahora = LocalDateTime.of(2026, 11, 2, 3, 0);
        when(reloj.ahora()).thenReturn(ahora);
        when(almacen.eliminarVencidos(ahora)).thenReturn(2);
        new PlanificadorSesiones(almacen, reloj).eliminarTokensVencidos();
        verify(almacen).eliminarVencidos(ahora);
    }
}
