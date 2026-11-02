package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.pagos.integracion.IntencionSimulada;
import java.time.LocalDateTime;

/**
 * Modelo de vista (record inmutable) de la intención de pago que muestra el simulador de pasarela P-27 (MODELO
 * 11.6.1).
 *
 * <p>Casos de uso: CU-35
 * @param idPreferencia identificador de la preferencia
 * @param concepto concepto (incluye el comercio, la cuota y el grupo)
 * @param importe importe a pagar
 * @param venceEn vencimiento de la intención (24 h, RN-22)
 */
public record ResumenIntencionSimulada(String idPreferencia, String concepto, Dinero importe,
                                       LocalDateTime venceEn) {

    /**
     * Fábrica a partir de la intención guardada por {@code PasarelaSimuladaAdapter}.
     *
     * <p>Casos de uso: CU-35
     * @param intencion la intención simulada
     * @return el resumen
     */
    public static ResumenIntencionSimulada desde(IntencionSimulada intencion) {
        return new ResumenIntencionSimulada(intencion.getIdPreferencia(), intencion.getConcepto(),
                intencion.getImporte(), intencion.getVenceEn());
    }
}
