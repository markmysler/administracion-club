package ar.edu.ub.club.grupos.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;
import ar.edu.ub.club.socios.ui.FilaSocio;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-11: Búsqueda de grupos y detalle del grupo familiar: integrantes, titular e
 * importe de la próxima cuota; acciones cambiar titular, quitar integrante y asociar un socio (busca primero el
 * socio y abre P-12 con este grupo preseleccionado como destino).
 *
 * <p>Interfaz pasiva del patrón MVP (Vista Pasiva): la implementa la vista y la usa el presentador.
 *
 * <p>Interfaz pasiva del patrón MVP (Vista Pasiva): la implementa la vista y la usa el presentador.
 *
 * <p>Interfaz pasiva del patrón MVP (Vista Pasiva): la implementa la vista y la usa el presentador.
 *
 * <p>Interfaz pasiva del patrón MVP (Vista Pasiva): la implementa la vista y la usa el presentador.
 *
 * <p>Casos de uso: todos
 */
public interface PantallaGrupoFamiliar extends PantallaBase {

    /**
     * Muestra los grupos encontrados.
     *
     * <p>Casos de uso: CU-08, CU-09, CU-10
     * @param filas valor de {@code filas}
     */
    void mostrarGrupos(List<FilaGrupo> filas);

    /**
     * Muestra el detalle del grupo familiar.
     *
     * <p>Casos de uso: CU-08, CU-09, CU-10
     * @param ficha valor de {@code ficha}
     */
    void mostrarGrupo(FichaGrupo ficha);

    /**
     * Muestra los socios encontrados para asociar a este grupo.
     *
     * <p>Casos de uso: CU-08, CU-09, CU-10
     * @param filas valor de {@code filas}
     */
    void mostrarSocios(List<FilaSocio> filas);

    /**
     * Crea y abre el diálogo P-12 con este grupo preseleccionado como destino.
     *
     * <p>Casos de uso: CU-08, CU-09, CU-10
     * @param idSocio valor de {@code idSocio}
     * @param nombre valor de {@code nombre}
     * @param dni valor de {@code dni}
     * @param idGrupoActual valor de {@code idGrupoActual}
     * @param grupoActual valor de {@code grupoActual}
     * @param idGrupoDestino valor de {@code idGrupoDestino}
     */
    void abrirAsociacion(Long idSocio, String nombre, String dni, Long idGrupoActual, String grupoActual, Long idGrupoDestino);
}
