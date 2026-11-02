package ar.edu.ub.club.grupos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import java.util.List;

/**
 * Grupos vigentes con al menos un integrante activo que todavía no tienen cuota del período. La usa
 * {@code ServicioCuotas.emitirCuotasDelMes} (emisión idempotente: un grupo que ya tiene la cuota no vuelve a
 * aparecer; además la base tiene {@code UNIQUE (grupo, período)}).
 *
 * <p>Objeto consulta (Query Object, Fowler) de entidades: su {@code sql()} se agrega a {@code consultaBase()} del
 * broker de {@code GrupoFamiliar} y el resultado pasa por el mapa de identidad. No es un bean: se crea con {@code new}.
 *
 * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
 */
public class GruposVigentesSinCuota extends ConsultaEntidades<GrupoFamiliar> {

    /** Período AAAA-MM. */
    private final String periodo;

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @param periodo período AAAA-MM
     */
    public GruposVigentesSinCuota(String periodo) {
        this.periodo = periodo;
    }

    /**
     * Clase de resultado: la resuelve el broker de {@code GrupoFamiliar}.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @return {@code GrupoFamiliar.class}
     */
    @Override
    public Class<GrupoFamiliar> claseResultado() {
        return GrupoFamiliar.class;
    }

    /**
     * Condición de vigencia, sin cuota del período y con integrantes activos.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @return condición y orden con parámetros {@code ?}
     */
    @Override
    public String sql() {
        return "WHERE grupo_familiar.vigente = true"
                + " AND NOT EXISTS (SELECT 1 FROM cuota WHERE cuota.grupo_familiar_id = grupo_familiar.id"
                + " AND cuota.periodo = ?)"
                + " AND EXISTS (SELECT 1 FROM socio JOIN usuario ON usuario.id = socio.id"
                + " WHERE socio.grupo_familiar_id = grupo_familiar.id AND usuario.estado = 'ACTIVA')"
                + " ORDER BY grupo_familiar.id";
    }

    /**
     * Valores de los parámetros en el orden de {@code sql()}.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(periodo);
    }
}
