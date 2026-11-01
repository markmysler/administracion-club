package ar.edu.ub.club.reservas.aplicacion;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.recursos.dominio.CanchaDesactivadaEvento;
import ar.edu.ub.club.recursos.dominio.DeporteDesactivadoEvento;
import ar.edu.ub.club.reservas.dominio.PublicadorEventosReserva;
import ar.edu.ub.club.reservas.dominio.Reserva;
import ar.edu.ub.club.socios.dominio.SocioDesactivadoEvento;
import java.util.List;

/**
 * Observador (patrón Observer) de los eventos de dominio de baja {@code SocioDesactivadoEvento}, {@code
 * CanchaDesactivadaEvento} y {@code DeporteDesactivadoEvento}. Se ejecuta de forma sincrónica dentro de la
 * transacción de la baja ({@code &#64;EventListener}), de modo que la baja y la cancelación de las reservas
 * futuras son atómicas (RN-16). Gracias a él, los módulos socios y recursos no dependen de reservas.
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23
 */
public class CanceladorReservasPorBaja {

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta ({@code ReservasFuturasDeSocio}, {@code ReservasFuturasDeCancha}, {@code ReservasFuturasDeDeporte}). */
    private FachadaPersistencia fachadaPersistencia;

    /** Publica {@code ReservaCanceladaEvento} por cada cancelación (notificación y auditoría). */
    private PublicadorEventosReserva publicador;

    /** Momento actual. */
    private Reloj reloj;

    /**
     * Cancela las reservas futuras confirmadas del socio con motivo «Baja de socio» y {@code canceladaPor =
     * evento.usuario}.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23
     * @param evento Socio dado de baja y cuenta que realizó la baja.
     */
    public void alDesactivarSocio(SocioDesactivadoEvento evento) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Cancela las reservas futuras de la cancha con motivo «Baja de cancha».
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23
     * @param evento Cancha dada de baja y cuenta.
     */
    public void alDesactivarCancha(CanchaDesactivadaEvento evento) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Cancela las reservas futuras de las canchas del deporte con motivo «Baja de deporte».
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23
     * @param evento Deporte dado de baja y cuenta.
     */
    public void alDesactivarDeporte(DeporteDesactivadoEvento evento) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23
     * @param reservas valor de {@code reservas}
     * @param motivo valor de {@code motivo}
     * @param por valor de {@code por}
     * @return el resultado
     */
    private Integer cancelarTodas(List<Reserva> reservas, String motivo, Usuario por) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
