package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;
import ar.edu.ub.club.socios.aplicacion.DatosCategoria;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-10: Listado de categorías con alta y edición en línea.
 *
 * <p>Casos de uso: CU-06
 */
public interface PantallaCategorias extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-06
     * @param filas valor de {@code filas}
     */
    void mostrarCategorias(List<FilaCategoria> filas);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-06
     * @param datos valor de {@code datos}
     * @param idCategoria valor de {@code idCategoria}
     */
    void editar(DatosCategoria datos, Long idCategoria);
}
