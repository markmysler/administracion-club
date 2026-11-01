package ar.edu.ub.club.reservas.persistencia;

import ar.edu.ub.club.compartido.dominio.ConsultaReservasFuturas;
import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.compartido.dominio.ResumenReserva;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class ConsultaReservasFuturasReservas implements ConsultaReservasFuturas {

    /** Atributo fachadaPersistencia. */
    private FachadaPersistencia fachadaPersistencia;

    /** Atributo reloj. */
    private Reloj reloj;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param id valor de {@code id}
     * @return el resultado
     */
    public List<ResumenReserva> buscarFuturasDeSocio(Long id) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param id valor de {@code id}
     * @return el resultado
     */
    public List<ResumenReserva> buscarFuturasDeCancha(Long id) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param id valor de {@code id}
     * @return el resultado
     */
    public List<ResumenReserva> buscarFuturasDeDeporte(Long id) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param fecha valor de {@code fecha}
     * @return el resultado
     */
    public List<ResumenReserva> buscarDeFecha(LocalDate fecha) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param ahora valor de {@code ahora}
     * @return el resultado
     */
    public List<ResumenReserva> buscarFuturasDesde(LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
