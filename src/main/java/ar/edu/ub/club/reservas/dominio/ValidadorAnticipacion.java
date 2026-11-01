package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.agenda.dominio.ReglaReserva;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import java.time.Duration;

/**
 * Eslabón concreto de la Cadena de Responsabilidad (posición 5) de RN-13 y RN-15: anticipación mínima (la de
 * modificación si la solicitud es una modificación) y máxima del turno.
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
public class ValidadorAnticipacion extends ValidadorReservaEncadenado {

    /** Anticipaciones configuradas. */
    private final ReglaReserva regla;

    /**
     * Crea el validador con la regla que obtuvo la fábrica.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param regla reglas de reserva
     */
    public ValidadorAnticipacion(ReglaReserva regla) {
        this.regla = regla;
    }

    /**
     * Calcula los minutos entre {@code s.ahora} y {@code s.turno.inicio()}. El mínimo es {@code
     * regla.anticipacionMinimaModificacionHoras} si {@code s.esModificacion()} y {@code
     * regla.anticipacionMinimaHoras} en otro caso. Si falta menos que el mínimo, o si la fecha del turno es
     * posterior a la fecha de {@code s.ahora} más {@code regla.anticipacionMaximaDias}, lanza {@code ErrorNegocio}
     * con la anticipación vigente.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param s solicitud
     * @throws ErrorNegocio si no se cumple la anticipación
     */
    @Override
    protected void verificar(SolicitudReserva s) {
        int minimaHoras = s.esModificacion()
                ? regla.getAnticipacionMinimaModificacionHoras()
                : regla.getAnticipacionMinimaHoras();
        int maximaDias = regla.getAnticipacionMaximaDias();
        long minutos = Duration.between(s.getAhora(), s.getTurno().inicio()).toMinutes();
        boolean muyPronto = minutos < minimaHoras * 60L;
        boolean muyLejos = s.getTurno().getFecha().isAfter(s.getAhora().toLocalDate().plusDays(maximaDias));
        if (muyPronto || muyLejos) {
            String accion = s.esModificacion() ? "modificar una reserva" : "reservar";
            throw new ErrorNegocio("Debe " + accion + " con al menos " + minimaHoras + " horas y hasta "
                    + maximaDias + " días de anticipación.");
        }
    }
}
