package ar.edu.ub.club.pagos.aplicacion;

import ar.edu.ub.club.pagos.dominio.MedioPago;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class DatosMedioPago {

    /** Atributo id. */
    private Long id;

    /** Atributo nombre. */
    private String nombre;

    /** Atributo habilitado. */
    private boolean habilitado;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param medio valor de {@code medio}
     * @return el resultado
     */
    public static DatosMedioPago desde(MedioPago medio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
