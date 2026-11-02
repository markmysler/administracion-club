package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;
import ar.edu.ub.club.socios.aplicacion.DatosCategoria;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-10: Listado de categorías con alta y edición en línea.
 *
 * <p>Interfaz pasiva del patrón MVP (Vista Pasiva): la implementa la vista y la usa el presentador.
 *
 * <p>Interfaz pasiva del patrón MVP (Vista Pasiva): la implementa la vista y la usa el presentador.
 *
 * <p>Casos de uso: CU-06
 */
public interface PantallaCategorias extends PantallaBase {

    /**
     * Muestra el listado de categorías con nombre, importe y estado.
     *
     * <p>Casos de uso: CU-06
     * @param filas valor de {@code filas}
     */
    void mostrarCategorias(List<FilaCategoria> filas);

    /**
     * Abre el formulario de la categoría, vacío (alta) o con los datos actuales.
     *
     * <p>Casos de uso: CU-06
     * @param datos valor de {@code datos}
     * @param idCategoria valor de {@code idCategoria}
     */
    void editar(DatosCategoria datos, Long idCategoria);
}
