package ar.edu.ub.club.agenda.aplicacion;

import ar.edu.ub.club.agenda.dominio.DiaSemana;
import ar.edu.ub.club.agenda.dominio.DisponibilidadClub;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Set;

/**
 * Objeto de comando de P-17 (CU-24): días de atención, horario de apertura y cierre y la marca {@code confirmar}
 * del protocolo de confirmación en dos pasos (false en el primer envío; true después de que el administrador
 * acepta las reservas que quedan fuera de la nueva disponibilidad). Lo vincula el {@code Binder} del formulario
 * y lo recibe {@code ServicioAgenda.configurarDisponibilidad}.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-24
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
     * Precarga el formulario de P-17 con la disponibilidad vigente (CU-24 paso 2), con {@code confirmar = false}.
     *
     * <p>Casos de uso: CU-24
     * @param disponibilidad disponibilidad del club
     * @return datos editables (copia de los días: el formulario no modifica la entidad)
     */
    public static DatosDisponibilidad desde(DisponibilidadClub disponibilidad) {
        DatosDisponibilidad datos = new DatosDisponibilidad();
        datos.diasAtencion = disponibilidad.getDiasAtencion().isEmpty()
                ? EnumSet.noneOf(DiaSemana.class) : EnumSet.copyOf(disponibilidad.getDiasAtencion());
        datos.horaApertura = disponibilidad.getHoraApertura();
        datos.horaCierre = disponibilidad.getHoraCierre();
        datos.confirmar = false;
        return datos;
    }

    /**
     * Devuelve {@code diasAtencion}: diasAtencion.
     *
     * <p>Casos de uso: CU-24
     * @return el valor del atributo
     */
    public Set<DiaSemana> getDiasAtencion() {
        return diasAtencion;
    }

    /**
     * Asigna {@code diasAtencion} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-24
     * @param diasAtencion nuevo valor
     */
    public void setDiasAtencion(Set<DiaSemana> diasAtencion) {
        this.diasAtencion = diasAtencion;
    }

    /**
     * Devuelve {@code horaApertura}: horaApertura.
     *
     * <p>Casos de uso: CU-24
     * @return el valor del atributo
     */
    public LocalTime getHoraApertura() {
        return horaApertura;
    }

    /**
     * Asigna {@code horaApertura} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-24
     * @param horaApertura nuevo valor
     */
    public void setHoraApertura(LocalTime horaApertura) {
        this.horaApertura = horaApertura;
    }

    /**
     * Devuelve {@code horaCierre}: horaCierre.
     *
     * <p>Casos de uso: CU-24
     * @return el valor del atributo
     */
    public LocalTime getHoraCierre() {
        return horaCierre;
    }

    /**
     * Asigna {@code horaCierre} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-24
     * @param horaCierre nuevo valor
     */
    public void setHoraCierre(LocalTime horaCierre) {
        this.horaCierre = horaCierre;
    }

    /**
     * Devuelve {@code confirmar}: confirmar.
     *
     * <p>Casos de uso: CU-24
     * @return el valor del atributo
     */
    public boolean getConfirmar() {
        return confirmar;
    }

    /**
     * Asigna {@code confirmar} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-24
     * @param confirmar nuevo valor
     */
    public void setConfirmar(boolean confirmar) {
        this.confirmar = confirmar;
    }
}
