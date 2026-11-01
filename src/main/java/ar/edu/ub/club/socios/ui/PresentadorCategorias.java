package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.socios.aplicacion.DatosCategoria;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;

/**
 * Presentador de la pantalla P-10: Listado de categorías con alta y edición en línea.
 *
 * <p>Casos de uso: CU-06
 */
public class PresentadorCategorias extends PresentadorBase<PantallaCategorias> {

    /** Atributo servicioSocios. */
    private ServicioSocios servicioSocios;

    /**
     * {@code ServicioSocios.listarCategorias(): List&lt;CategoriaSocio&gt;} → {@code FilaCategoria.desde(c)}
     *
     * <p>Casos de uso: CU-06
     */
    public void cargar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * precarga {@code new DatosCategoria(fila.nombre(), fila.importe(), fila.activa())} o un {@code DatosCategoria}
     * vacío
     *
     * <p>Casos de uso: CU-06
     * @param fila valor de {@code fila}
     */
    public void alEditar(FilaCategoria fila) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioSocios.registrarCategoria(datos)} o {@code modificarCategoria(idCategoria, datos)}; luego
     * {@code cargar()}
     *
     * <p>Casos de uso: CU-06
     * @param idCategoria valor de {@code idCategoria}
     * @param datos valor de {@code datos}
     */
    public void alGuardar(Long idCategoria, DatosCategoria datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
