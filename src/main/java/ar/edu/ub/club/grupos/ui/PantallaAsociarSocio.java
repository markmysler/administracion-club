package ar.edu.ub.club.grupos.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-12: Diálogo que se abre desde la ficha del socio que se asocia (P-08), como
 * indica CU-07: busca el grupo destino por DNI o nombre de un integrante o del titular y asocia el socio a ese
 * grupo.
 *
 * <p>Interfaz pasiva del patrón MVP (Vista Pasiva): la implementa la vista y la usa el presentador.
 *
 * <p>Interfaz pasiva del patrón MVP (Vista Pasiva): la implementa la vista y la usa el presentador.
 *
 * <p>Interfaz pasiva del patrón MVP (Vista Pasiva): la implementa la vista y la usa el presentador.
 *
 * <p>Interfaz pasiva del patrón MVP (Vista Pasiva): la implementa la vista y la usa el presentador.
 *
 * <p>Casos de uso: CU-07
 */
public interface PantallaAsociarSocio extends PantallaBase {

    /**
     * Muestra el socio que se asocia y su grupo actual.
     *
     * <p>Casos de uso: CU-07
     * @param nombre valor de {@code nombre}
     * @param dni valor de {@code dni}
     * @param grupoActual valor de {@code grupoActual}
     */
    void mostrarSocio(String nombre, String dni, String grupoActual);

    /**
     * Muestra los grupos vigentes encontrados.
     *
     * <p>Casos de uso: CU-07
     * @param filas valor de {@code filas}
     */
    void mostrarGrupos(List<FilaGrupo> filas);

    /**
     * Fija el grupo destino elegido en P-11.
     *
     * <p>Casos de uso: CU-07
     * @param idGrupoDestino valor de {@code idGrupoDestino}
     */
    void preseleccionar(Long idGrupoDestino);

    /**
     * Cierra el diálogo y dispara {@code DialogoResueltoEvento}.
     *
     * <p>Casos de uso: CU-07
     */
    void cerrarConExito();
}
