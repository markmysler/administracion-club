package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-06: Listado y búsqueda de socios con acceso a la ficha y al alta.
 *
 * <p>Interfaz pasiva del patrón MVP (Vista Pasiva): la implementa la vista y la usa el presentador.
 *
 * <p>Casos de uso: CU-01, CU-04
 */
public interface PantallaListadoSocios extends PantallaBase {

    /**
     * Muestra los socios encontrados.
     *
     * <p>Casos de uso: CU-04
     * @param filas valor de {@code filas}
     */
    void mostrarSocios(List<FilaSocio> filas);
}
