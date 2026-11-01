package ar.edu.ub.club.recursos.ui;

import ar.edu.ub.club.compartido.excepciones.ConfirmacionRequeridaException;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.recursos.aplicacion.DatosCancha;
import ar.edu.ub.club.recursos.aplicacion.ServicioRecursos;

/**
 * Presentador de la pantalla P-16: Listado de canchas por deporte con alta, edición y desactivación con
 * advertencia.
 *
 * <p>Casos de uso: CU-21, CU-22, CU-23
 */
public class PresentadorCanchas extends PresentadorBase<PantallaCanchas> {

    /** Atributo servicioRecursos. */
    private ServicioRecursos servicioRecursos;

    /**
     * {@code ServicioRecursos.listarDeportes()} (→ {@code OpcionSeleccion}) y {@code
     * ServicioRecursos.listarCanchas(idDeporte): List&lt;Cancha&gt;} → {@code FilaCancha.desde(c)}
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param idDeporte valor de {@code idDeporte}
     */
    public void cargar(Long idDeporte) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * precarga {@code new DatosCancha(fila.nombre(), fila.idDeporte(), fila.ubicacion(), fila.caracteristicas(),
     * fila.activa(), false)}
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param fila valor de {@code fila}
     */
    public void alEditar(FilaCancha fila) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioRecursos.registrarCancha(datos)} o {@code modificarCancha(idCancha, datos)}; {@code
     * ConfirmacionRequeridaException} (cambio de deporte con reservas) → confirma y reenvía con {@code
     * datos.confirmar = true}
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param idCancha valor de {@code idCancha}
     * @param datos valor de {@code datos}
     */
    public void alGuardar(Long idCancha, DatosCancha datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioRecursos.desactivarCancha(idCancha, false): ResultadoBaja}
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param idCancha valor de {@code idCancha}
     */
    public void alDesactivar(Long idCancha) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioRecursos.desactivarCancha(idCancha, true)}
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param idCancha valor de {@code idCancha}
     */
    public void alConfirmarDesactivacion(Long idCancha) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-22
     * @param ex valor de {@code ex}
     */
    protected void alRequerirConfirmacion(ConfirmacionRequeridaException ex) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
