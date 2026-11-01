package ar.edu.ub.club.agenda.ui;

import ar.edu.ub.club.agenda.aplicacion.DatosDisponibilidad;
import ar.edu.ub.club.agenda.aplicacion.DatosFeriado;
import ar.edu.ub.club.agenda.aplicacion.ServicioAgenda;
import ar.edu.ub.club.agenda.dominio.DisponibilidadClub;
import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.compartido.excepciones.ConfirmacionRequeridaException;
import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Presentador de la pantalla P-17: Configuración de días y horario de atención y lista de feriados.
 * Patrón MVP (Vista Pasiva): llama a {@code ServicioAgenda} dentro de {@code ejecutar}/{@code consultar} y arma
 * {@code DatosDisponibilidad} y {@code FilaFeriado}. Redefine el gancho {@code alRequerirConfirmacion} de
 * {@code PresentadorBase}: muestra las reservas afectadas por el cambio de horario o por el feriado.
 *
 * <p>Casos de uso: CU-24
 */
@SpringComponent
@UIScope
public class PresentadorDisponibilidadClub extends PresentadorBase<PantallaDisponibilidadClub> {

    /** Fachada del módulo agenda. */
    private final ServicioAgenda servicioAgenda;

    /**
     * Crea el presentador con la fachada (inyección por constructor).
     *
     * <p>Casos de uso: CU-24
     * @param servicioAgenda fachada del módulo agenda
     */
    public PresentadorDisponibilidadClub(ServicioAgenda servicioAgenda) {
        this.servicioAgenda = servicioAgenda;
    }

    /**
     * {@code ServicioAgenda.consultarDisponibilidadClub(): DisponibilidadClub} → {@code
     * DatosDisponibilidad.desde(d)} y {@code FilaFeriado.desde(f)} de {@code d.feriados} (solo los futuros, 07A);
     * {@code ServicioAgenda.consultarReglas(): ReglaReserva} para acotar el calendario de feriados a la
     * anticipación máxima (RN-13) → {@code pantalla.mostrarRangoCalendario(desde, hasta)}
     *
     * <p>Casos de uso: CU-24
     */
    public void cargar() {
        LocalDate hoy = LocalDate.now(RelojSistema.ZONA_CLUB);
        Optional<DisponibilidadClub> disponibilidad = consultar(servicioAgenda::consultarDisponibilidadClub);
        disponibilidad.ifPresent(d -> pantalla.mostrarDisponibilidad(DatosDisponibilidad.desde(d),
                d.getFeriados().stream().filter(f -> f.getFecha().isAfter(hoy)).map(FilaFeriado::desde).toList()));
        consultar(servicioAgenda::consultarReglas).ifPresent(regla ->
                pantalla.mostrarRangoCalendario(hoy.plusDays(1), hoy.plusDays(regla.getAnticipacionMaximaDias())));
    }

    /**
     * {@code ServicioAgenda.configurarDisponibilidad(datos)}; {@code ConfirmacionRequeridaException} → confirma y
     * reenvía con {@code confirmar = true}
     *
     * <p>Casos de uso: CU-24
     * @param datos días, horario y {@code confirmar}
     */
    public void alGuardarHorario(DatosDisponibilidad datos) {
        if (ejecutar(() -> servicioAgenda.configurarDisponibilidad(datos))) {
            cargar();
            pantalla.mostrarMensaje("Disponibilidad del club guardada.");
        }
    }

    /**
     * {@code ServicioAgenda.registrarFeriado(datos)} (misma confirmación)
     *
     * <p>Casos de uso: CU-24
     * @param datos fecha, descripción y {@code confirmar}
     */
    public void alAgregarFeriado(DatosFeriado datos) {
        if (ejecutar(() -> servicioAgenda.registrarFeriado(datos))) {
            cargar();
            pantalla.mostrarMensaje("Feriado registrado.");
        }
    }

    /**
     * {@code ServicioAgenda.eliminarFeriado(idFeriado)}
     *
     * <p>Casos de uso: CU-24
     * @param idFeriado feriado a eliminar
     */
    public void alEliminarFeriado(Long idFeriado) {
        if (ejecutar(() -> servicioAgenda.eliminarFeriado(idFeriado))) {
            cargar();
            pantalla.mostrarMensaje("Feriado eliminado.");
        }
    }

    /**
     * Gancho de {@code PresentadorBase}: muestra las reservas afectadas (que se conservan) con
     * {@code pantalla.pedirConfirmacion(afectadas)} (FA5 6.1 y FA1 3.3).
     *
     * <p>Casos de uso: CU-24
     * @param ex excepción con las reservas afectadas
     */
    @Override
    protected void alRequerirConfirmacion(ConfirmacionRequeridaException ex) {
        List<FilaResumenReserva> afectadas = ex.getReservasAfectadas().stream().map(FilaResumenReserva::desde).toList();
        pantalla.pedirConfirmacion(afectadas);
    }
}
