package ar.edu.ub.club.agenda.aplicacion;

import ar.edu.ub.club.agenda.dominio.DiaSemana;
import ar.edu.ub.club.agenda.dominio.DisponibilidadClub;
import java.time.LocalTime;
import java.util.Set;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class DatosDisponibilidad {

    /** Atributo diasAtencion. */
    private Set<DiaSemana> diasAtencion;

    /** Atributo horaApertura. */
    private LocalTime horaApertura;

    /** Atributo horaCierre. */
    private LocalTime horaCierre;

    /** Atributo confirmar. */
    private boolean confirmar;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param disponibilidad valor de {@code disponibilidad}
     * @return el resultado
     */
    public static DatosDisponibilidad desde(DisponibilidadClub disponibilidad) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code diasAtencion}: diasAtencion.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Set<DiaSemana> getDiasAtencion() {
        return diasAtencion;
    }

    /**
     * Asigna {@code diasAtencion} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param diasAtencion nuevo valor
     */
    public void setDiasAtencion(Set<DiaSemana> diasAtencion) {
        this.diasAtencion = diasAtencion;
    }

    /**
     * Devuelve {@code horaApertura}: horaApertura.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public LocalTime getHoraApertura() {
        return horaApertura;
    }

    /**
     * Asigna {@code horaApertura} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param horaApertura nuevo valor
     */
    public void setHoraApertura(LocalTime horaApertura) {
        this.horaApertura = horaApertura;
    }

    /**
     * Devuelve {@code horaCierre}: horaCierre.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public LocalTime getHoraCierre() {
        return horaCierre;
    }

    /**
     * Asigna {@code horaCierre} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param horaCierre nuevo valor
     */
    public void setHoraCierre(LocalTime horaCierre) {
        this.horaCierre = horaCierre;
    }

    /**
     * Devuelve {@code confirmar}: confirmar.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public boolean getConfirmar() {
        return confirmar;
    }

    /**
     * Asigna {@code confirmar} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param confirmar nuevo valor
     */
    public void setConfirmar(boolean confirmar) {
        this.confirmar = confirmar;
    }
}
