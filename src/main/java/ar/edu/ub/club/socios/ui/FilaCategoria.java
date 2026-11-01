package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.socios.dominio.CategoriaSocio;

/**
 * Fila del listado de categorías (P-10): nombre, importe y estado.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador con {@code desde(categoria)}.
 *
 * <p>Casos de uso: CU-06
 *
 * @param id categoría
 * @param nombre nombre
 * @param importe importe mensual
 * @param activa si es asignable
 */
public record FilaCategoria(Long id, String nombre, Dinero importe, boolean activa) {

    /**
     * Construye la fila.
     *
     * <p>Casos de uso: CU-06
     * @param categoria categoría
     * @return fila
     */
    public static FilaCategoria desde(CategoriaSocio categoria) {
        return new FilaCategoria(categoria.getId(), categoria.getNombre(), categoria.getImporte(), categoria.estaActiva());
    }
}
