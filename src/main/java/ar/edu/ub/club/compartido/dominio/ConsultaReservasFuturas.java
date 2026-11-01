package ar.edu.ub.club.compartido.dominio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaz del paquete {@code compartido.dominio} (consulta invertida, D-10) que usan {@code ServicioSocios},
 * {@code ServicioRecursos} y {@code ServicioAgenda} para informar, antes de confirmar una operación, las reservas
 * futuras afectadas (RN-16, CU-22, CU-24). La implementa {@code ConsultaReservasFuturasReservas} de
 * {@code reservas.persistencia} con objetos consulta de proyección ({@code ResumenesFuturosDeSocio},
 * {@code ResumenesFuturosDeCancha}, {@code ResumenesFuturosDeDeporte}, {@code ResumenesDeFecha},
 * {@code ResumenesFuturosDesde}).
 *
 * <p>Casos de uso: CU-03, CU-04, CU-20, CU-22, CU-23, CU-24
 */
public interface ConsultaReservasFuturas {

    /**
     * Reservas futuras confirmadas del socio.
     *
     * <p>Casos de uso: CU-03, CU-04
     * @param id socio
     * @return lista de {@code ResumenReserva}
     */
    List<ResumenReserva> buscarFuturasDeSocio(Long id);

    /**
     * Reservas futuras confirmadas de la cancha.
     *
     * <p>Casos de uso: CU-22, CU-23
     * @param id cancha
     * @return lista de {@code ResumenReserva}
     */
    List<ResumenReserva> buscarFuturasDeCancha(Long id);

    /**
     * Reservas futuras confirmadas de las canchas del deporte.
     *
     * <p>Casos de uso: CU-20
     * @param id deporte
     * @return lista de {@code ResumenReserva}
     */
    List<ResumenReserva> buscarFuturasDeDeporte(Long id);

    /**
     * Reservas confirmadas de una fecha futura (registro de feriado).
     *
     * <p>Casos de uso: CU-24
     * @param fecha fecha
     * @return lista de {@code ResumenReserva}
     */
    List<ResumenReserva> buscarDeFecha(LocalDate fecha);

    /**
     * Reservas futuras confirmadas (cambio de disponibilidad).
     *
     * <p>Casos de uso: CU-24
     * @param ahora momento actual
     * @return lista de {@code ResumenReserva}
     */
    List<ResumenReserva> buscarFuturasDesde(LocalDateTime ahora);
}
