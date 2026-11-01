package ar.edu.ub.club.grupos.aplicacion;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class DetalleGrupo {

    /** Atributo grupo. */
    private GrupoFamiliar grupo;

    /** Atributo importeProximaCuota. */
    private Dinero importeProximaCuota;

    /**
     * Devuelve {@code grupo}: grupo.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public GrupoFamiliar getGrupo() {
        return grupo;
    }

    /**
     * Devuelve {@code importeProximaCuota}: importeProximaCuota.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Dinero getImporteProximaCuota() {
        return importeProximaCuota;
    }
}
