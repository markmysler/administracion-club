package ar.edu.ub.club.reservas.dominio;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import ar.edu.ub.club.accesos.dominio.Administrador;
import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.recursos.dominio.Cancha;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.mockito.quality.Strictness;

/**
 * Dobles de prueba (Mockito) de las clases de otros módulos que usa el dominio de reservas: así las pruebas no
 * dependen de la implementación de agenda, socios ni recursos ni de la base de datos.
 *
 * <p>Casos de uso: CU-27, CU-28, CU-30, CU-32, CU-33
 */
final class Dobles {

    /**
     * Clase de utilidades.
     *
     * <p>Casos de uso: ninguno (pruebas)
     */
    private Dobles() {
    }

    /**
     * Turno con fecha y horario, {@code inicio()}, {@code fin()}, {@code duracionMinutos()} y {@code
     * seSuperponeCon} (intervalos semiabiertos, RN-12).
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param fecha fecha
     * @param inicio hora de inicio
     * @param fin hora de fin
     * @return turno simulado
     */
    static Turno turno(LocalDate fecha, LocalTime inicio, LocalTime fin) {
        Turno t = mock(Turno.class, withSettings().strictness(Strictness.LENIENT));
        when(t.getFecha()).thenReturn(fecha);
        when(t.getHoraInicio()).thenReturn(inicio);
        when(t.getHoraFin()).thenReturn(fin);
        when(t.inicio()).thenReturn(LocalDateTime.of(fecha, inicio));
        when(t.fin()).thenReturn(LocalDateTime.of(fecha, fin));
        when(t.duracionMinutos()).thenReturn((int) Duration.between(inicio, fin).toMinutes());
        when(t.seSuperponeCon(any())).thenAnswer(inv -> {
            Turno otro = inv.getArgument(0);
            return fecha.equals(otro.getFecha()) && LocalDateTime.of(fecha, inicio).isBefore(otro.fin())
                    && otro.inicio().isBefore(LocalDateTime.of(fecha, fin));
        });
        return t;
    }

    /**
     * Socio con identificador y estado de la cuenta.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-30
     * @param id identificador
     * @param activo true si la cuenta está activa
     * @return socio simulado
     */
    static Socio socio(Long id, boolean activo) {
        Socio s = mock(Socio.class, withSettings().strictness(Strictness.LENIENT));
        when(s.getId()).thenReturn(id);
        when(s.estaActiva()).thenReturn(activo);
        return s;
    }

    /**
     * Cancha con identificador y disponibilidad para reservas.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param id identificador
     * @param disponible true si cancha y deporte están activos
     * @return cancha simulada
     */
    static Cancha cancha(Long id, boolean disponible) {
        Cancha c = mock(Cancha.class, withSettings().strictness(Strictness.LENIENT));
        when(c.getId()).thenReturn(id);
        when(c.estaDisponibleParaReservar()).thenReturn(disponible);
        return c;
    }

    /**
     * Administrador simulado.
     *
     * <p>Casos de uso: CU-28, CU-32, CU-33
     * @return administrador
     */
    static Administrador administrador() {
        return mock(Administrador.class);
    }
}
