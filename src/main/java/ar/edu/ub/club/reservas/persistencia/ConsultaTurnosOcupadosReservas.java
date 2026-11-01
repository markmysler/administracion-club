package ar.edu.ub.club.reservas.persistencia;

import ar.edu.ub.club.agenda.dominio.ConsultaTurnosOcupados;
import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Implementación, en {@code reservas.persistencia}, de la consulta invertida {@code ConsultaTurnosOcupados}
 * (agenda.dominio, Entrega 4B, D-10): {@code agenda} obtiene los turnos ocupados sin depender de {@code reservas}.
 * {@code turnosOcupados(idCancha, fecha)} → {@code fachadaPersistencia.buscar(TurnosOcupadosDeCancha(idCancha,
 * fecha))}. Se ejecuta dentro de la transacción de la fachada que la invoca ({@code ServicioAgenda}, {@code
 * FabricaCadenaValidacion}).
 *
 * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
 */
@Component
public class ConsultaTurnosOcupadosReservas implements ConsultaTurnosOcupados {

    /** Único punto de acceso a la persistencia («Singleton»). */
    private final FachadaPersistencia fachadaPersistencia;

    /**
     * Crea la consulta (bean de Spring).
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @param fachadaPersistencia fachada de persistencia
     */
    public ConsultaTurnosOcupadosReservas(FachadaPersistencia fachadaPersistencia) {
        this.fachadaPersistencia = fachadaPersistencia;
    }

    /**
     * Turnos de las reservas CONFIRMADAS de la cancha en la fecha.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28, CU-32
     * @param idCancha cancha
     * @param fecha fecha
     * @return lista de {@code Turno}
     */
    @Override
    public List<Turno> turnosOcupados(Long idCancha, LocalDate fecha) {
        return fachadaPersistencia.buscar(new TurnosOcupadosDeCancha(idCancha, fecha));
    }
}
