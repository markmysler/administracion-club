package ar.edu.ub.club.reportes.persistencia;

import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.reportes.aplicacion.FilaCuotaDeudor;
import ar.edu.ub.club.reportes.aplicacion.FilaUsoCancha;
import ar.edu.ub.club.reportes.aplicacion.FiltroReporte;
import ar.edu.ub.club.reportes.aplicacion.Indicadores;
import java.time.LocalDate;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Casos de uso: CU-38, CU-39, CU-40
 */
public class ConsultasReportes {

    /** Atributo fachadaPersistencia. */
    private FachadaPersistencia fachadaPersistencia;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-38
     * @param filtro valor de {@code filtro}
     * @return el resultado
     */
    public List<FilaUsoCancha> usoCanchas(FiltroReporte filtro) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-39
     * @param filtro valor de {@code filtro}
     * @return el resultado
     */
    public List<FilaCuotaDeudor> cuotasYDeudores(FiltroReporte filtro) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-40
     * @param hoy valor de {@code hoy}
     * @return el resultado
     */
    public Indicadores indicadores(LocalDate hoy) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
