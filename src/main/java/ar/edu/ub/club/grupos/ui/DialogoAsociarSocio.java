package ar.edu.ub.club.grupos.ui;

import ar.edu.ub.club.compartido.ui.DialogoBase;
import java.util.List;

/**
 * Vista de la pantalla P-12: Diálogo que se abre desde la ficha del socio que se asocia (P-08), como indica
 * CU-07: busca el grupo destino por DNI o nombre de un integrante o del titular y asocia el socio a ese grupo.
 *
 * <p>Casos de uso: CU-07
 */
public class DialogoAsociarSocio extends DialogoBase implements PantallaAsociarSocio {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-07
     * @param nombre valor de {@code nombre}
     * @param dni valor de {@code dni}
     * @param grupoActual valor de {@code grupoActual}
     */
    @Override
    public void mostrarSocio(String nombre, String dni, String grupoActual) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-07
     * @param filas valor de {@code filas}
     */
    @Override
    public void mostrarGrupos(List<FilaGrupo> filas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-07
     * @param idGrupoDestino valor de {@code idGrupoDestino}
     */
    @Override
    public void preseleccionar(Long idGrupoDestino) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-07
     */
    @Override
    public void cerrarConExito() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
