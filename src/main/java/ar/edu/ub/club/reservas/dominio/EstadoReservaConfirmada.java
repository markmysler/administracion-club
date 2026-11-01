package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.recursos.dominio.Cancha;
import java.time.LocalDateTime;

/**
 * Estado concreto del patrón State: reserva vigente. Es el único estado que redefine las tres transiciones
 * ({@code cancelar}, {@code modificar} y {@code finalizar}); modifica la reserva solo con sus métodos de paquete
 * ({@code cambiarEstado}, {@code registrarCancelacion}, {@code cambiarTurno}).
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33; tareas programadas: finalización de
 * reservas cumplidas
 */
public class EstadoReservaConfirmada extends EstadoReserva {

    /**
     * Crea la instancia única (la guarda {@code EstadoReserva}).
     *
     * <p>Casos de uso: CU-27, CU-28
     */
    EstadoReservaConfirmada() {
        super();
    }

    /**
     * Código persistido.
     *
     * <p>Casos de uso: todos
     * @return «CONFIRMADA»
     */
    @Override
    public String codigo() {
        return "CONFIRMADA";
    }

    /**
     * Confirmada → Cancelada: si el turno ya comenzó lanza {@code ErrorNegocio}; si no, registra el motivo y la
     * cuenta que cancela y cambia al estado Cancelada. El turno queda libre (RN-14, RN-15, RN-16).
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-30, CU-33
     * @param r reserva a cancelar
     * @param motivo motivo de la cancelación
     * @param por cuenta que cancela (socio dueño o administrador)
     * @param ahora momento de la operación
     * @throws ErrorNegocio si el turno ya comenzó
     */
    @Override
    public void cancelar(Reserva r, String motivo, Usuario por, LocalDateTime ahora) {
        if (r.haComenzado(ahora)) {
            throw new ErrorNegocio("La reserva ya comenzó y no puede cancelarse.");
        }
        r.registrarCancelacion(motivo, por);
        r.cambiarEstado(EstadoReserva.desdeCodigo("CANCELADA"));
    }

    /**
     * Cambia el turno y la cancha y la reserva permanece Confirmada; si el turno ya comenzó lanza
     * {@code ErrorNegocio} (RN-15).
     *
     * <p>Casos de uso: CU-32
     * @param r reserva a modificar
     * @param nuevoTurno turno validado por la cadena
     * @param nuevaCancha cancha validada por la cadena
     * @param ahora momento de la operación
     * @throws ErrorNegocio si el turno ya comenzó
     */
    @Override
    public void modificar(Reserva r, Turno nuevoTurno, Cancha nuevaCancha, LocalDateTime ahora) {
        if (r.haComenzado(ahora)) {
            throw new ErrorNegocio("La reserva ya comenzó y no puede modificarse.");
        }
        r.cambiarTurno(nuevoTurno, nuevaCancha);
    }

    /**
     * Confirmada → Finalizada (el turno ya terminó).
     *
     * <p>Casos de uso: tareas programadas: finalización de reservas cumplidas
     * @param r reserva cuyo turno terminó
     */
    @Override
    public void finalizar(Reserva r) {
        r.cambiarEstado(EstadoReserva.desdeCodigo("FINALIZADA"));
    }
}
