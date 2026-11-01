package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.socios.dominio.CategoriaSocio;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-06
 */
public record FilaCategoria(Long id, String nombre, Dinero importe, boolean activa) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-06
     * @param categoria valor de {@code categoria}
     * @return el resultado
     */
    public static FilaCategoria desde(CategoriaSocio categoria) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
