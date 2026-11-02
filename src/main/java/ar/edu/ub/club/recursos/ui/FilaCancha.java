package ar.edu.ub.club.recursos.ui;

import ar.edu.ub.club.recursos.dominio.Cancha;

/**
 * Fila de la grilla de canchas de P-16: nombre, deporte, ubicación, características y estado; conserva el id del
 * deporte para precargar el formulario de edición (CU-22).
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-21, CU-22, CU-23
 *
 * @param id identificador de la cancha
 * @param nombre nombre
 * @param idDeporte identificador del deporte
 * @param deporte nombre del deporte
 * @param ubicacion ubicación (puede ser null)
 * @param caracteristicas características (puede ser null)
 * @param activa si admite reservas nuevas
 */
public record FilaCancha(Long id, String nombre, Long idDeporte, String deporte, String ubicacion, String caracteristicas, boolean activa) {

    /**
     * Arma la fila a partir de la cancha (y de su deporte, ya resuelto por {@code BrokerCancha}).
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     * @param cancha cancha
     * @return la fila
     */
    public static FilaCancha desde(Cancha cancha) {
        return new FilaCancha(cancha.getId(), cancha.getNombre(), cancha.getDeporte().getId(),
                cancha.getDeporte().getNombre(), cancha.getUbicacion(), cancha.getCaracteristicas(),
                cancha.estaActivo());
    }
}
