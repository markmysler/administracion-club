package ar.edu.ub.club.reportes.aplicacion;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Totales de un reporte: pares nombre → valor en el orden en que se muestran (por ejemplo «Reservas» → 5). Los calcula
 * el paso {@code calcularTotales} del método plantilla {@code Reporte.generar} y se muestran como pie del reporte en
 * pantalla y en los archivos exportados.
 *
 * <p>Objeto de datos de salida de la capa de aplicación.
 *
 * <p>Casos de uso: CU-38, CU-39
 */
public class Totales {

    /** Valores de los totales, por nombre, en orden de presentación. */
    private Map<String, Object> valores;

    /**
     * Crea los totales con una copia ordenada de los valores recibidos.
     *
     * <p>Agregado en la implementación: constructor con el atributo del diseño.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param valores valores por nombre (se conserva el orden de iteración)
     */
    public Totales(Map<String, Object> valores) {
        this.valores = new LinkedHashMap<>(valores);
    }

    /**
     * Devuelve los valores de los totales por nombre, en orden de presentación.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @return mapa nombre → valor
     */
    public Map<String, Object> getValores() {
        return valores;
    }
}
