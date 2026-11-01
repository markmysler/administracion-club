package ar.edu.ub.club.recursos.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.recursos.aplicacion.DatosDeporte;
import ar.edu.ub.club.recursos.aplicacion.ServicioRecursos;

/**
 * Presentador de la pantalla P-15: Listado de deportes con alta, edición y desactivación con advertencia.
 *
 * <p>Casos de uso: CU-18, CU-19, CU-20
 */
public class PresentadorDeportes extends PresentadorBase<PantallaDeportes> {

    /** Atributo servicioRecursos. */
    private ServicioRecursos servicioRecursos;

    /**
     * {@code ServicioRecursos.listarDeportes(): List&lt;Deporte&gt;} y {@code ServicioRecursos.listarCanchas(null):
     * List&lt;Cancha&gt;} (agrupadas por deporte) → {@code FilaDeporte.desde(d, cantidadCanchas)}
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     */
    public void cargar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * precarga {@code new DatosDeporte(fila.nombre(), fila.descripcion(), fila.activo())}
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     * @param fila valor de {@code fila}
     */
    public void alEditar(FilaDeporte fila) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioRecursos.registrarDeporte(datos)} o {@code modificarDeporte(idDeporte, datos)}
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     * @param idDeporte valor de {@code idDeporte}
     * @param datos valor de {@code datos}
     */
    public void alGuardar(Long idDeporte, DatosDeporte datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioRecursos.desactivarDeporte(idDeporte, false): ResultadoBaja}; si no {@code realizada}, {@code
     * pantalla.pedirConfirmacionBaja(idDeporte, afectadas)}
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     * @param idDeporte valor de {@code idDeporte}
     */
    public void alDesactivar(Long idDeporte) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioRecursos.desactivarDeporte(idDeporte, true)}
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     * @param idDeporte valor de {@code idDeporte}
     */
    public void alConfirmarDesactivacion(Long idDeporte) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
