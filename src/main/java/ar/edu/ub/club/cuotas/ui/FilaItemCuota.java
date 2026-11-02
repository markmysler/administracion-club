package ar.edu.ub.club.cuotas.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.cuotas.dominio.ItemCuota;

/**
 * Aporte de un integrante a una cuota (detalle de la fila en P-25): socio, categoría e importe al emitir.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-05, CU-35, CU-36
 * @param socio nombre del integrante
 * @param categoria categoría al emitir
 * @param importe importe al emitir
 */
public record FilaItemCuota(String socio, String categoria, Dinero importe) {

    /**
     * Convierte el ítem en fila.
     *
     * <p>Casos de uso: CU-05, CU-35, CU-36
     * @param item ítem de la cuota
     * @return la fila
     */
    public static FilaItemCuota desde(ItemCuota item) {
        String socio = item.getSocio() == null ? "" : item.getSocio().nombreCompleto();
        String categoria = item.getCategoria() == null ? "" : item.getCategoria().getNombre();
        return new FilaItemCuota(socio, categoria, item.getImporte());
    }
}
