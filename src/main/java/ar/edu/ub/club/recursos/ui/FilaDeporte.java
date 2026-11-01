package ar.edu.ub.club.recursos.ui;

import ar.edu.ub.club.recursos.dominio.Deporte;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: PENDIENTE
 */
public record FilaDeporte(Long id, String nombre, String descripcion, boolean activo, Integer cantidadCanchas) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param deporte valor de {@code deporte}
     * @param cantidadCanchas valor de {@code cantidadCanchas}
     * @return el resultado
     */
    public static FilaDeporte desde(Deporte deporte, Integer cantidadCanchas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
