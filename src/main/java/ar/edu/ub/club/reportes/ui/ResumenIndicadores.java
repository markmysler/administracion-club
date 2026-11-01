package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.reportes.aplicacion.Indicadores;
import java.time.LocalDate;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-40
 */
public record ResumenIndicadores(LocalDate fecha, Long reservasHoy, Long reservasSemana, Dinero recaudacionMes, Long cuotasPagadasMes, Long sociosActivos, Long cuotasPendientes, Long cuotasVencidas, List<String> canchasMasUsadas, List<String> noDisponibles) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-40
     * @param indicadores valor de {@code indicadores}
     * @return el resultado
     */
    public static ResumenIndicadores desde(Indicadores indicadores) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
