package ar.edu.ub.club.reservas.persistencia;

import ar.edu.ub.club.compartido.dominio.ConsultaReservasFuturas;
import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.compartido.dominio.ResumenReserva;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Implementación, en {@code reservas.persistencia}, de la consulta invertida {@code ConsultaReservasFuturas}
 * (compartido.dominio, Entrega 4B, D-10), de modo que {@code socios}, {@code recursos} y {@code agenda} listan las
 * reservas afectadas por una baja o un cambio de configuración sin depender de {@code reservas}. Cada método
 * resuelve un objeto consulta de proyección ({@code ResumenesFuturosDeSocio}, {@code ResumenesFuturosDeCancha},
 * {@code ResumenesFuturosDeDeporte}, {@code ResumenesDeFecha} y {@code ResumenesFuturosDesde}) dentro de la
 * transacción de la fachada que la invoca.
 *
 * <p>Casos de uso: CU-03, CU-04, CU-05, CU-20, CU-22, CU-23, CU-24
 */
@Component
public class ConsultaReservasFuturasReservas implements ConsultaReservasFuturas {

    /** Único punto de acceso a la persistencia («Singleton»). */
    private final FachadaPersistencia fachadaPersistencia;

    /** Momento actual (define qué reserva es futura). */
    private final Reloj reloj;

    /**
     * Crea la consulta (bean de Spring).
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-20, CU-22, CU-23, CU-24
     * @param fachadaPersistencia fachada de persistencia
     * @param reloj fecha y hora actual
     */
    public ConsultaReservasFuturasReservas(FachadaPersistencia fachadaPersistencia, Reloj reloj) {
        this.fachadaPersistencia = fachadaPersistencia;
        this.reloj = reloj;
    }

    /**
     * Reservas futuras confirmadas del socio.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05
     * @param id socio
     * @return lista de {@code ResumenReserva}
     */
    @Override
    public List<ResumenReserva> buscarFuturasDeSocio(Long id) {
        return fachadaPersistencia.buscar(new ResumenesFuturosDeSocio(id, reloj.ahora()));
    }

    /**
     * Reservas futuras confirmadas de la cancha.
     *
     * <p>Casos de uso: CU-22, CU-23
     * @param id cancha
     * @return lista de {@code ResumenReserva}
     */
    @Override
    public List<ResumenReserva> buscarFuturasDeCancha(Long id) {
        return fachadaPersistencia.buscar(new ResumenesFuturosDeCancha(id, reloj.ahora()));
    }

    /**
     * Reservas futuras confirmadas de las canchas del deporte.
     *
     * <p>Casos de uso: CU-20
     * @param id deporte
     * @return lista de {@code ResumenReserva}
     */
    @Override
    public List<ResumenReserva> buscarFuturasDeDeporte(Long id) {
        return fachadaPersistencia.buscar(new ResumenesFuturosDeDeporte(id, reloj.ahora()));
    }

    /**
     * Reservas confirmadas de una fecha (registro de un feriado).
     *
     * <p>Casos de uso: CU-24
     * @param fecha fecha
     * @return lista de {@code ResumenReserva}
     */
    @Override
    public List<ResumenReserva> buscarDeFecha(LocalDate fecha) {
        return fachadaPersistencia.buscar(new ResumenesDeFecha(fecha));
    }

    /**
     * Reservas futuras confirmadas (cambio de disponibilidad).
     *
     * <p>Casos de uso: CU-24
     * @param ahora momento actual
     * @return lista de {@code ResumenReserva}
     */
    @Override
    public List<ResumenReserva> buscarFuturasDesde(LocalDateTime ahora) {
        return fachadaPersistencia.buscar(new ResumenesFuturosDesde(ahora));
    }
}
