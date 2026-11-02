package ar.edu.ub.club.compartido.excepciones;

import ar.edu.ub.club.compartido.dominio.ResumenReserva;
import java.util.List;

/**
 * Excepción de {@code compartido.excepciones} que lanzan las fachadas cuando una modificación que no es una baja
 * tiene reservas afectadas y se pidió sin {@code confirmar = true} (cambio de deporte de una cancha; cambio de
 * disponibilidad; registro de feriado). No modifica datos: la transacción se revierte. El presentador la atiende
 * en {@code alRequerirConfirmacion}: muestra las reservas afectadas y reenvía con {@code confirmar = true}. Las
 * bajas devuelven {@code ResultadoBaja}.
 *
 * <p>Casos de uso: CU-22, CU-24
 */
public class ConfirmacionRequeridaException extends RuntimeException {

    /** Reservas que quedarán fuera de la nueva configuración (se conservan). */
    private final List<ResumenReserva> reservasAfectadas;

    /**
     * Crea la excepción con las reservas afectadas que el usuario debe confirmar.
     *
     * <p>Casos de uso: CU-22, CU-24
     * @param reservasAfectadas reservas futuras afectadas por el cambio
     */
    public ConfirmacionRequeridaException(List<ResumenReserva> reservasAfectadas) {
        super("La operación afecta reservas futuras y requiere confirmación.");
        this.reservasAfectadas = List.copyOf(reservasAfectadas);
    }

    /**
     * Devuelve las reservas afectadas.
     *
     * <p>Casos de uso: CU-22, CU-24
     * @return lista inmutable de resúmenes
     */
    public List<ResumenReserva> getReservasAfectadas() {
        return reservasAfectadas;
    }
}
