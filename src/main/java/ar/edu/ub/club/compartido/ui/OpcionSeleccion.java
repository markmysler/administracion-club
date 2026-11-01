package ar.edu.ub.club.compartido.ui;

/**
 * Modelo de vista común (record inmutable) de una opción de combo: deportes, canchas, categorías, medios y socios.
 * Lo arma el presentador desde la entidad; la vista solo muestra el texto.
 *
 * <p>Casos de uso: CU-01, CU-02, CU-21, CU-22, CU-26, CU-28, CU-31, CU-32, CU-36, CU-38
 *
 * @param id identificador de la entidad
 * @param texto texto visible
 */
public record OpcionSeleccion(Long id, String texto) {

    /**
     * Texto que muestran los combos de Vaadin.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-21, CU-22, CU-26, CU-28, CU-31, CU-32, CU-36, CU-38
     * @return texto visible
     */
    @Override
    public String toString() {
        return texto;
    }
}
