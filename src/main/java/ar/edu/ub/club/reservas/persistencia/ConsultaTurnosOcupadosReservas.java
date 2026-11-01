package ar.edu.ub.club.reservas.persistencia;

import ar.edu.ub.club.agenda.dominio.ConsultaTurnosOcupados;
import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementaciones, en {@code reservas.persistencia}, de las consultas invertidas {@code ConsultaTurnosOcupados}
 * (agenda.dominio) y {@code ConsultaReservasFuturas} (compartido.dominio) (Entrega 4B, D-10). Atributos: {@code
 * - fachadaPersistencia: FachadaPersistencia} y, en la segunda, {@code - reloj: Reloj}. {@code
 * ConsultaTurnosOcupadosReservas.turnosOcupados(idCancha: Long, fecha: Date): List&lt;Turno&gt;} → {@code
 * fachadaPersistencia.buscar(TurnosOcupadosDeCancha(idCancha, fecha))}. {@code ConsultaReservasFuturasReservas}
 * implementa {@code buscarFuturasDeSocio}, {@code buscarFuturasDeCancha}, {@code buscarFuturasDeDeporte}, {@code
 * buscarDeFecha} y {@code buscarFuturasDesde} con las consultas de proyección {@code ResumenesFuturosDeSocio},
 * {@code ResumenesFuturosDeCancha}, {@code ResumenesFuturosDeDeporte}, {@code ResumenesDeFecha} y {@code
 * ResumenesFuturosDesde}.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class ConsultaTurnosOcupadosReservas implements ConsultaTurnosOcupados {

    /** Atributo fachadaPersistencia. */
    private FachadaPersistencia fachadaPersistencia;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param idCancha valor de {@code idCancha}
     * @param fecha valor de {@code fecha}
     * @return el resultado
     */
    public List<Turno> turnosOcupados(Long idCancha, LocalDate fecha) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
