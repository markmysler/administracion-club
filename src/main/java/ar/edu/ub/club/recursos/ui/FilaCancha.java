package ar.edu.ub.club.recursos.ui;

import ar.edu.ub.club.recursos.dominio.Cancha;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: PENDIENTE
 */
public record FilaCancha(Long id, String nombre, Long idDeporte, String deporte, String ubicacion, String caracteristicas, boolean activa) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param cancha valor de {@code cancha}
     * @return el resultado
     */
    public static FilaCancha desde(Cancha cancha) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
