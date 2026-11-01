package ar.edu.ub.club.agenda.ui;

import ar.edu.ub.club.agenda.aplicacion.DatosDisponibilidad;
import ar.edu.ub.club.agenda.aplicacion.DatosFeriado;
import ar.edu.ub.club.agenda.aplicacion.ServicioAgenda;
import ar.edu.ub.club.compartido.excepciones.ConfirmacionRequeridaException;
import ar.edu.ub.club.compartido.ui.PresentadorBase;

/**
 * Presentador de la pantalla P-17: Configuración de días y horario de atención y lista de feriados.
 *
 * <p>Casos de uso: todos
 */
public class PresentadorDisponibilidadClub extends PresentadorBase<PantallaDisponibilidadClub> {

    /** Atributo servicioAgenda. */
    private ServicioAgenda servicioAgenda;

    /**
     * {@code ServicioAgenda.consultarDisponibilidadClub(): DisponibilidadClub} → {@code
     * DatosDisponibilidad.desde(d)} y {@code FilaFeriado.desde(f)} de {@code d.feriados}; {@code
     * ServicioAgenda.consultarReglas(): ReglaReserva} para acotar el calendario de feriados a la anticipación máxima
     * (RN-13) → {@code pantalla.mostrarRangoCalendario(desde, hasta)}
     *
     * <p>Casos de uso: CU-24
     */
    public void cargar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioAgenda.configurarDisponibilidad(datos)}; {@code ConfirmacionRequeridaException} → confirma y
     * reenvía con {@code confirmar = true}
     *
     * <p>Casos de uso: CU-24
     * @param datos valor de {@code datos}
     */
    public void alGuardarHorario(DatosDisponibilidad datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioAgenda.registrarFeriado(datos)} (misma confirmación)
     *
     * <p>Casos de uso: CU-24
     * @param datos valor de {@code datos}
     */
    public void alAgregarFeriado(DatosFeriado datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioAgenda.eliminarFeriado(idFeriado)}
     *
     * <p>Casos de uso: CU-24
     * @param idFeriado valor de {@code idFeriado}
     */
    public void alEliminarFeriado(Long idFeriado) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-24
     * @param ex valor de {@code ex}
     */
    protected void alRequerirConfirmacion(ConfirmacionRequeridaException ex) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
