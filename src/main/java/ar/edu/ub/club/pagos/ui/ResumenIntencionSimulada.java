package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.pagos.integracion.IntencionSimulada;
import java.time.LocalDateTime;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-35
 */
public record ResumenIntencionSimulada(String idPreferencia, String concepto, Dinero importe, LocalDateTime venceEn) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-35
     * @param intencion valor de {@code intencion}
     * @return el resultado
     */
    public static ResumenIntencionSimulada desde(IntencionSimulada intencion) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
