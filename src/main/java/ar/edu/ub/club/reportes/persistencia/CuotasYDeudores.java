package ar.edu.ub.club.reportes.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaProyeccion;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.reportes.aplicacion.FilaCuotaDeudor;
import ar.edu.ub.club.reportes.aplicacion.FiltroReporte;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto consulta (Query Object, Fowler) de proyección; lo resuelve {@code FachadaPersistencia} sin broker.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class CuotasYDeudores extends ConsultaProyeccion<FilaCuotaDeudor> {

    /** Atributo filtro. */
    private FiltroReporte filtro;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    public Class<FilaCuotaDeudor> claseResultado() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param fila valor de {@code fila}
     * @return el resultado
     */
    @Override
    public FilaCuotaDeudor leerFila(FilaResultado fila) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    @Override
    public String sql() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    @Override
    public List<Object> parametros() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
