package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PantallaBase;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-08: Ficha del socio con su grupo, cuotas y reservas futuras; acciones
 * modificar, ver grupo, asociar a otro grupo y desactivar.
 *
 * <p>Casos de uso: CU-02, CU-03, CU-04, CU-07
 */
public interface PantallaFichaSocio extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03, CU-04
     * @param detalle valor de {@code detalle}
     */
    void mostrarFicha(DetalleSocio detalle);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03, CU-04
     * @param idSocio valor de {@code idSocio}
     * @param nombre valor de {@code nombre}
     * @param dni valor de {@code dni}
     * @param candidatosTitular valor de {@code candidatosTitular}
     */
    void abrirDesactivacion(Long idSocio, String nombre, String dni, List<OpcionSeleccion> candidatosTitular);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03, CU-04
     * @param idSocio valor de {@code idSocio}
     * @param nombre valor de {@code nombre}
     * @param dni valor de {@code dni}
     * @param idGrupoActual valor de {@code idGrupoActual}
     * @param grupoActual valor de {@code grupoActual}
     * @param idGrupoDestino valor de {@code idGrupoDestino}
     */
    void abrirAsociacion(Long idSocio, String nombre, String dni, Long idGrupoActual, String grupoActual, Long idGrupoDestino);
}
