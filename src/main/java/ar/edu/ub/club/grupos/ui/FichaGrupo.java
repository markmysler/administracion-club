package ar.edu.ub.club.grupos.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.grupos.aplicacion.DetalleGrupo;
import java.time.LocalDate;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-08, CU-09, CU-10
 */
public record FichaGrupo(Long id, LocalDate fechaCreacion, boolean vigente, String titular, Dinero importeProximaCuota, List<FilaIntegrante> integrantes) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-08, CU-09, CU-10
     * @param detalle valor de {@code detalle}
     * @return el resultado
     */
    public static FichaGrupo desde(DetalleGrupo detalle) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
