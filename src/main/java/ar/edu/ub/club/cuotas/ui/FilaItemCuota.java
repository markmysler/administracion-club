package ar.edu.ub.club.cuotas.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.cuotas.dominio.ItemCuota;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: PENDIENTE
 */
public record FilaItemCuota(String socio, String categoria, Dinero importe) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param item valor de {@code item}
     * @return el resultado
     */
    public static FilaItemCuota desde(ItemCuota item) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
