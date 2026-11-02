package ar.edu.ub.club.grupos.aplicacion;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;

/**
 * Objeto de datos de salida de {@code ServicioGruposFamiliares} (consultarGrupo, asociarSocio, cambiarTitular y
 * quitarSocio): el grupo con sus integrantes y el importe de la próxima cuota ({@code calcularImporteCuota()}).
 * Lo convierte {@code FichaGrupo.desde(detalle)}. Inmutable, sin comportamiento.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-07, CU-08, CU-09, CU-10
 */
public class DetalleGrupo {

    /** Atributo grupo. */
    private GrupoFamiliar grupo;

    /** Atributo importeProximaCuota. */
    private Dinero importeProximaCuota;

    /**
     * Crea el detalle.
     *
     * <p>Agregado en la implementación: constructor (el modelo no lo declaraba; los diagramas dibujan
     * {@code DetalleGrupo(g, importeProximaCuota)}).
     *
     * <p>Casos de uso: CU-07, CU-08, CU-09, CU-10
     * @param grupo grupo familiar
     * @param importeProximaCuota suma de las categorías de los integrantes activos
     */
    public DetalleGrupo(GrupoFamiliar grupo, Dinero importeProximaCuota) {
        this.grupo = grupo;
        this.importeProximaCuota = importeProximaCuota;
    }

    /**
     * Devuelve {@code grupo}: grupo.
     *
     * <p>Casos de uso: CU-07, CU-08, CU-09, CU-10
     * @return el valor del atributo
     */
    public GrupoFamiliar getGrupo() {
        return grupo;
    }

    /**
     * Devuelve {@code importeProximaCuota}: importeProximaCuota.
     *
     * <p>Casos de uso: CU-07, CU-08, CU-09, CU-10
     * @return el valor del atributo
     */
    public Dinero getImporteProximaCuota() {
        return importeProximaCuota;
    }
}
