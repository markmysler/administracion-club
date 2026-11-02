package ar.edu.ub.club.recursos.ui;

import ar.edu.ub.club.recursos.dominio.Deporte;

/**
 * Fila de la grilla de deportes de P-15: nombre, descripción, estado y cantidad de canchas asociadas.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-18, CU-19, CU-20
 *
 * @param id identificador del deporte
 * @param nombre nombre
 * @param descripcion descripción (puede ser null)
 * @param activo si admite reservas nuevas
 * @param cantidadCanchas cantidad de canchas del deporte (activas e inactivas)
 */
public record FilaDeporte(Long id, String nombre, String descripcion, boolean activo, Integer cantidadCanchas) {

    /**
     * Arma la fila a partir del deporte y de la cantidad de canchas que contó {@code PresentadorDeportes.cargar}.
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     * @param deporte deporte
     * @param cantidadCanchas cantidad de canchas del deporte
     * @return la fila
     */
    public static FilaDeporte desde(Deporte deporte, Integer cantidadCanchas) {
        return new FilaDeporte(deporte.getId(), deporte.getNombre(), deporte.getDescripcion(), deporte.estaActivo(),
                cantidadCanchas);
    }
}
