package ar.edu.ub.club.grupos.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.grupos.aplicacion.DetalleGrupo;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import java.time.LocalDate;
import java.util.List;

/**
 * Detalle del grupo familiar (P-11): fecha de creación, vigencia, titular, importe de la próxima cuota e
 * integrantes con su estado.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador con {@code desde(detalle)}.
 *
 * <p>Casos de uso: CU-07, CU-08, CU-09, CU-10
 *
 * @param id grupo
 * @param fechaCreacion creación
 * @param vigente vigencia (no vigente: solo consulta histórica, CU-10 FA2)
 * @param titular apellido y nombre del titular (vacío si no tiene)
 * @param importeProximaCuota suma de las categorías de los integrantes activos
 * @param integrantes integrantes
 */
public record FichaGrupo(Long id, LocalDate fechaCreacion, boolean vigente, String titular, Dinero importeProximaCuota, List<FilaIntegrante> integrantes) {

    /**
     * Construye el modelo desde el detalle que devuelve {@code ServicioGruposFamiliares}.
     *
     * <p>Casos de uso: CU-07, CU-08, CU-09, CU-10
     * @param detalle detalle del grupo
     * @return modelo de vista
     */
    public static FichaGrupo desde(DetalleGrupo detalle) {
        GrupoFamiliar grupo = detalle.getGrupo();
        List<FilaIntegrante> integrantes = grupo.getIntegrantes().stream()
                .map(s -> FilaIntegrante.desde(s, grupo.getTitular())).toList();
        return new FichaGrupo(grupo.getId(), grupo.getFechaCreacion(), Boolean.TRUE.equals(grupo.getVigente()),
                grupo.getTitular() == null ? "" : grupo.getTitular().nombreCompleto(),
                detalle.getImporteProximaCuota(), integrantes);
    }
}
