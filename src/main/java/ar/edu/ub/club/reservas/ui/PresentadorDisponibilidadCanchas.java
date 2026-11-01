package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.agenda.aplicacion.FiltroDisponibilidad;
import ar.edu.ub.club.agenda.aplicacion.ServicioAgenda;
import ar.edu.ub.club.agenda.dominio.ReglaReserva;
import ar.edu.ub.club.agenda.dominio.TurnoDisponible;
import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.cuotas.ui.FilaCuota;
import ar.edu.ub.club.recursos.aplicacion.ServicioRecursos;
import ar.edu.ub.club.recursos.dominio.Cancha;
import ar.edu.ub.club.recursos.dominio.Deporte;
import ar.edu.ub.club.socios.aplicacion.CriterioBusquedaSocio;
import ar.edu.ub.club.socios.aplicacion.FichaSocio;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;
import ar.edu.ub.club.socios.ui.FilaSocio;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Presentador de la pantalla P-19 (MVP, Vista Pasiva): grilla de turnos por cancha y horario para una fecha, con
 * filtros por deporte y cancha; para el administrador, la elección del socio de una reserva manual. No usa tipos
 * de Vaadin; cada llamada a una fachada va dentro de {@code ejecutar}/{@code consultar} de {@code PresentadorBase}
 * (sec_cu26_p1, sec_cu27_p1, sec_cu28_p1, sec_cu28_p2).
 *
 * <p>Casos de uso: CU-26, CU-27, CU-28
 */
@SpringComponent
@UIScope
public class PresentadorDisponibilidadCanchas extends PresentadorBase<PantallaDisponibilidadCanchas> {

    /** Fachada de recursos (deportes y canchas de los filtros). */
    private final ServicioRecursos servicioRecursos;

    /** Fachada de agenda (reglas y disponibilidad). */
    private final ServicioAgenda servicioAgenda;

    /** Fachada de socios (búsqueda y ficha del socio de una reserva manual). */
    private final ServicioSocios servicioSocios;

    /** Cuenta de la sesión. */
    private final UsuarioActual usuarioActual;

    /** Verificación polimórfica de permisos (¿es administrativo?). */
    private final ControlAcceso controlAcceso;

    /** Socio elegido por el administrador para la reserva manual ({@code null}: reserva online del socio). */
    private Long idSocioElegido;

    /**
     * Crea el presentador (bean de Spring de alcance de UI).
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param servicioRecursos fachada de recursos
     * @param servicioAgenda fachada de agenda
     * @param servicioSocios fachada de socios
     * @param usuarioActual cuenta de la sesión
     * @param controlAcceso verificación de permisos
     */
    public PresentadorDisponibilidadCanchas(ServicioRecursos servicioRecursos, ServicioAgenda servicioAgenda,
                                            ServicioSocios servicioSocios, UsuarioActual usuarioActual,
                                            ControlAcceso controlAcceso) {
        this.servicioRecursos = servicioRecursos;
        this.servicioAgenda = servicioAgenda;
        this.servicioSocios = servicioSocios;
        this.usuarioActual = usuarioActual;
        this.controlAcceso = controlAcceso;
    }

    /**
     * {@code ServicioRecursos.listarDeportes()} y {@code listarCanchas(null)} (filtros; sin deportes activos informa
     * que no hay canchas, FA1); {@code ServicioAgenda.consultarReglas(): ReglaReserva} para acotar el calendario de
     * fechas a la anticipación mínima y máxima (RN-13) → {@code pantalla.mostrarRangoCalendario(desde, hasta)};
     * {@code pantalla.mostrarEleccionSocio(controlAcceso.tienePermiso(usuarioActual.obtener(),
     * GESTIONAR_RESERVAS_CLUB))}.
     *
     * <p>Casos de uso: CU-26, CU-28
     */
    public void cargar() {
        idSocioElegido = null;
        Optional<List<Deporte>> deportes = consultar(servicioRecursos::listarDeportes);
        if (deportes.isEmpty()) {
            return;
        }
        List<OpcionSeleccion> opcionesDeporte = deportes.get().stream().filter(Deporte::estaActivo)
                .map(d -> new OpcionSeleccion(d.getId(), d.getNombre())).toList();
        if (opcionesDeporte.isEmpty()) {
            pantalla.mostrarMensaje("No hay canchas disponibles para reservar.");
            return;
        }
        List<OpcionSeleccion> opcionesCancha = consultar(() -> servicioRecursos.listarCanchas(null))
                .map(this::opcionesCancha).orElse(List.of());
        pantalla.mostrarFiltros(opcionesDeporte, opcionesCancha);
        consultar(servicioAgenda::consultarReglas).ifPresent(this::mostrarRango);
        pantalla.mostrarEleccionSocio(esAdministrativo());
    }

    /**
     * {@code ServicioRecursos.listarCanchas(idDeporte)} → {@code pantalla.mostrarCanchas(opciones)}.
     *
     * <p>Casos de uso: CU-26
     * @param idDeporte deporte elegido ({@code null}: todos)
     */
    public void alCambiarDeporte(Long idDeporte) {
        consultar(() -> servicioRecursos.listarCanchas(idDeporte))
                .ifPresent(canchas -> pantalla.mostrarCanchas(opcionesCancha(canchas)));
    }

    /**
     * {@code ServicioAgenda.consultarDisponibilidad(filtro): List<TurnoDisponible>} → {@code FilaTurno.desde(t,
     * esAdministrador)} ({@code esAdministrador} = {@code controlAcceso.tienePermiso(usuarioActual.obtener(),
     * GESTIONAR_RESERVAS_CLUB)}); los errores de la fecha (FA2 a FA4) los muestra {@code consultar}.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param filtro fecha, deporte y cancha
     */
    public void alConsultar(FiltroDisponibilidad filtro) {
        if (filtro == null || filtro.getFecha() == null) {
            pantalla.mostrarError("Elija la fecha a consultar.");
            return;
        }
        boolean esAdministrador = esAdministrativo();
        consultar(() -> servicioAgenda.consultarDisponibilidad(filtro)).ifPresent(turnos -> {
            List<FilaTurno> filas = turnos.stream().map((TurnoDisponible t) -> FilaTurno.desde(t, esAdministrador)).toList();
            pantalla.mostrarTurnos(filas);
            if (filas.stream().noneMatch(f -> "LIBRE".equals(f.estado()))) {
                pantalla.mostrarMensaje("No hay turnos libres para la fecha elegida.");
            }
        });
    }

    /**
     * {@code ServicioSocios.buscarSocios(criterio)} → {@code FilaSocio}; sin coincidencias lo informa (CU-28 FA1).
     *
     * <p>Casos de uso: CU-28
     * @param criterio texto de búsqueda (DNI o apellido)
     */
    public void alBuscarSocio(CriterioBusquedaSocio criterio) {
        consultar(() -> servicioSocios.buscarSocios(criterio)).ifPresent(socios -> {
            pantalla.mostrarSocios(socios.stream().map(FilaSocio::desde).toList());
            if (socios.isEmpty()) {
                pantalla.mostrarMensaje("No hay socios que coincidan con la búsqueda.");
            }
        });
    }

    /**
     * {@code ServicioSocios.consultarSocio(idSocio): FichaSocio} → {@code
     * pantalla.mostrarSocioElegido(FilaSocio.desde(ficha.socio), FilaCuota.desde(cuotaVigente, false))}, con la
     * cuota más reciente de {@code ficha.estadoCuenta} («Cuota del grupo»; null si no hay). Guarda el socio
     * elegido para la confirmación.
     *
     * <p>Casos de uso: CU-28
     * @param idSocio socio elegido
     */
    public void alElegirSocio(Long idSocio) {
        consultar(() -> servicioSocios.consultarSocio(idSocio)).ifPresent((FichaSocio ficha) -> {
            idSocioElegido = idSocio;
            Cuota vigente = ficha.getEstadoCuenta() == null ? null : ficha.getEstadoCuenta().stream()
                    .filter(c -> c.getFechaEmision() != null)
                    .max(Comparator.comparing(Cuota::getFechaEmision)).orElse(null);
            pantalla.mostrarSocioElegido(FilaSocio.desde(ficha.getSocio()),
                    vigente == null ? null : FilaCuota.desde(vigente, false));
        });
    }

    /**
     * {@code pantalla.abrirConfirmacion(turno, idSocioElegido)} (P-20). Solo un turno LIBRE puede reservarse; el
     * administrador debe elegir antes el socio.
     *
     * <p>Casos de uso: CU-27, CU-28
     * @param turno turno elegido en la grilla
     */
    public void alElegirTurno(FilaTurno turno) {
        if (turno == null || !"LIBRE".equals(turno.estado())) {
            pantalla.mostrarError("Elija un turno libre.");
            return;
        }
        if (idSocioElegido == null && esAdministrativo()) {
            pantalla.mostrarError("Elija primero el socio de la reserva.");
            return;
        }
        pantalla.abrirConfirmacion(turno, idSocioElegido);
    }

    /**
     * Indica si la cuenta de la sesión tiene GESTIONAR_RESERVAS_CLUB (ve el ocupante y registra reservas
     * manuales).
     *
     * <p>Casos de uso: CU-26, CU-28
     * @return true si es administrativa
     */
    private boolean esAdministrativo() {
        return controlAcceso.tienePermiso(usuarioActual.obtener(), Permiso.GESTIONAR_RESERVAS_CLUB);
    }

    /**
     * Opciones del filtro de cancha: solo las que admiten reservas.
     *
     * <p>Casos de uso: CU-26
     * @param canchas canchas de recursos
     * @return opciones de selección
     */
    private List<OpcionSeleccion> opcionesCancha(List<Cancha> canchas) {
        return canchas.stream().filter(Cancha::estaDisponibleParaReservar)
                .map(c -> new OpcionSeleccion(c.getId(), c.getNombre())).toList();
    }

    /**
     * Calcula el rango de fechas consultables (desde la anticipación mínima hasta la máxima, RN-13) en la zona del
     * club y lo entrega a la pantalla.
     *
     * <p>Casos de uso: CU-26
     * @param regla reglas de reserva
     */
    private void mostrarRango(ReglaReserva regla) {
        LocalDateTime ahora = LocalDateTime.now(RelojSistema.ZONA_CLUB);
        LocalDate desde = ahora.plusHours(regla.getAnticipacionMinimaHoras()).toLocalDate();
        LocalDate hasta = ahora.toLocalDate().plusDays(regla.getAnticipacionMaximaDias());
        pantalla.mostrarRangoCalendario(desde, hasta);
    }
}
