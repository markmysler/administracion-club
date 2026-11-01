package ar.edu.ub.club.recursos.ui;

import ar.edu.ub.club.compartido.dominio.ResultadoBaja;
import ar.edu.ub.club.compartido.dominio.ResumenReserva;
import ar.edu.ub.club.compartido.excepciones.ConfirmacionRequeridaException;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.recursos.aplicacion.DatosCancha;
import ar.edu.ub.club.recursos.aplicacion.ServicioRecursos;
import ar.edu.ub.club.recursos.dominio.Deporte;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.util.List;

/**
 * Presentador de la pantalla P-16: Listado de canchas por deporte con alta, edición y desactivación con
 * advertencia. Patrón MVP (Vista Pasiva): recibe los eventos de {@code VistaCanchas}, llama a
 * {@code ServicioRecursos} dentro de {@code ejecutar}/{@code consultar} y convierte las entidades en
 * {@code FilaCancha} y {@code OpcionSeleccion}. Redefine el gancho {@code alRequerirConfirmacion} (Template Method
 * de {@code PresentadorBase}) para el cambio de deporte con reservas futuras (CU-22 FA6).
 *
 * <p>Casos de uso: CU-21, CU-22, CU-23
 */
@SpringComponent
@UIScope
public class PresentadorCanchas extends PresentadorBase<PantallaCanchas> {

    /** Campo del formulario que identifica al deporte (lo usa el {@code ErrorNegocio} de deporte inactivo). */
    private static final String CAMPO_DEPORTE = "idDeporte";

    /** Fachada del módulo recursos. */
    private final ServicioRecursos servicioRecursos;

    /** Deporte por el que está filtrada la grilla (null = todos); se conserva al recargar después de guardar. */
    private Long idDeporteFiltrado;

    /**
     * Crea el presentador con la fachada (inyección por constructor).
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param servicioRecursos fachada del módulo recursos
     */
    public PresentadorCanchas(ServicioRecursos servicioRecursos) {
        this.servicioRecursos = servicioRecursos;
    }

    /**
     * {@code ServicioRecursos.listarDeportes()} (→ {@code OpcionSeleccion}) y {@code
     * ServicioRecursos.listarCanchas(idDeporte): List&lt;Cancha&gt;} → {@code FilaCancha.desde(c)}
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param idDeporte deporte del filtro (null = todas las canchas)
     */
    public void cargar(Long idDeporte) {
        this.idDeporteFiltrado = idDeporte;
        mostrarDeportesActivos();
        consultar(() -> servicioRecursos.listarCanchas(idDeporte).stream().map(FilaCancha::desde).toList())
                .ifPresent(pantalla::mostrarCanchas);
    }

    /**
     * precarga {@code new DatosCancha(fila.nombre(), fila.idDeporte(), fila.ubicacion(), fila.caracteristicas(),
     * fila.activa(), false)}; con {@code fila = null} (Nueva cancha) abre el formulario vacío si hay deportes
     * activos y, si no, informa que primero debe registrarse o reactivarse uno (CU-21 FA1).
     *
     * <p>Casos de uso: CU-21, CU-22
     * @param fila fila elegida o null para un alta
     */
    public void alEditar(FilaCancha fila) {
        if (fila != null) {
            pantalla.editar(new DatosCancha(fila.nombre(), fila.idDeporte(), fila.ubicacion(), fila.caracteristicas(),
                    fila.activa(), false), fila.id());
            return;
        }
        List<OpcionSeleccion> activos = mostrarDeportesActivos();
        if (activos != null && activos.isEmpty()) {
            pantalla.mostrarMensaje("Primero debe registrarse o reactivarse un deporte.");
        } else if (activos != null) {
            pantalla.editar(new DatosCancha(), null);
        }
    }

    /**
     * {@code ServicioRecursos.registrarCancha(datos)} o {@code modificarCancha(idCancha, datos)}; {@code
     * ConfirmacionRequeridaException} (cambio de deporte con reservas) → confirma y reenvía con {@code
     * datos.confirmar = true}. Si el deporte resultó inactivo (FA5) vuelve a cargar los deportes activos.
     *
     * <p>Casos de uso: CU-21, CU-22
     * @param idCancha cancha que se modifica (null en el alta)
     * @param datos datos del formulario
     */
    public void alGuardar(Long idCancha, DatosCancha datos) {
        boolean guardado = ejecutar(() -> {
            try {
                if (idCancha == null) {
                    servicioRecursos.registrarCancha(datos);
                } else {
                    servicioRecursos.modificarCancha(idCancha, datos);
                }
            } catch (ErrorNegocio ex) {
                if (CAMPO_DEPORTE.equals(ex.getCampo())) {
                    mostrarDeportesActivos();
                }
                throw ex;
            }
        });
        if (guardado) {
            cargar(idDeporteFiltrado);
            pantalla.mostrarMensaje(idCancha == null ? "Cancha registrada." : "Cancha modificada.");
        }
    }

    /**
     * {@code ServicioRecursos.desactivarCancha(idCancha, false): ResultadoBaja}; si no {@code realizada},
     * {@code pantalla.pedirConfirmacionBaja(idCancha, afectadas)}.
     *
     * <p>Casos de uso: CU-23
     * @param idCancha cancha a desactivar
     */
    public void alDesactivar(Long idCancha) {
        consultar(() -> servicioRecursos.desactivarCancha(idCancha, false)).ifPresent(resultado -> {
            if (resultado.realizada()) {
                informarBaja(resultado);
            } else {
                pantalla.pedirConfirmacionBaja(idCancha, filas(resultado.reservasAfectadas()));
            }
        });
    }

    /**
     * {@code ServicioRecursos.desactivarCancha(idCancha, true)}; recarga la grilla e informa la cantidad de
     * reservas canceladas (paso 10).
     *
     * <p>Casos de uso: CU-23
     * @param idCancha cancha a desactivar
     */
    public void alConfirmarDesactivacion(Long idCancha) {
        consultar(() -> servicioRecursos.desactivarCancha(idCancha, true)).ifPresent(this::informarBaja);
    }

    /**
     * Gancho de {@code PresentadorBase}: ante el cambio de deporte de una cancha con reservas futuras (CU-22 FA6)
     * muestra esas reservas, que se conservan, con {@code pantalla.pedirConfirmacionCambio(afectadas)}.
     *
     * <p>Casos de uso: CU-22
     * @param ex excepción con las reservas afectadas
     */
    @Override
    protected void alRequerirConfirmacion(ConfirmacionRequeridaException ex) {
        pantalla.pedirConfirmacionCambio(filas(ex.getReservasAfectadas()));
    }

    /**
     * Obtiene los deportes activos, los muestra como {@code OpcionSeleccion} y los devuelve.
     *
     * <p>Casos de uso: CU-21, CU-22
     * @return opciones mostradas, o null si la consulta falló (el error ya se informó)
     */
    private List<OpcionSeleccion> mostrarDeportesActivos() {
        List<OpcionSeleccion> opciones = consultar(() -> servicioRecursos.listarDeportes().stream()
                .filter(Deporte::estaActivo)
                .map(d -> new OpcionSeleccion(d.getId(), d.getNombre()))
                .toList()).orElse(null);
        if (opciones != null) {
            pantalla.mostrarDeportes(opciones);
        }
        return opciones;
    }

    /**
     * Recarga la grilla e informa la baja con la cantidad de reservas canceladas (CU-23 paso 10).
     *
     * <p>Casos de uso: CU-23
     * @param resultado resultado de la baja realizada
     */
    private void informarBaja(ResultadoBaja resultado) {
        cargar(idDeporteFiltrado);
        pantalla.mostrarMensaje("Cancha desactivada. Reservas canceladas: " + resultado.reservasAfectadas().size() + ".");
    }

    /**
     * Convierte las reservas afectadas en modelos de vista ({@code FilaResumenReserva.desde(r)} por cada una).
     *
     * <p>Casos de uso: CU-22, CU-23
     * @param reservas reservas afectadas
     * @return filas para el diálogo
     */
    private static List<FilaResumenReserva> filas(List<ResumenReserva> reservas) {
        return reservas.stream().map(FilaResumenReserva::desde).toList();
    }
}
