package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;

/**
 * Medio de pago manual (efectivo, transferencia, etc.) habilitable en CU-34.
 *
 * <p>Entidad del dominio; la persiste su broker.
 *
 * <p>Casos de uso: todos
 */
public class MedioPago extends EntidadPersistente {

    /** Nombre del medio. */
    private String nombre;

    /** Si puede usarse en pagos nuevos. */
    private Boolean habilitado;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param id valor de {@code id}
     * @param nombre valor de {@code nombre}
     * @param habilitado valor de {@code habilitado}
     * @return el resultado
     */
    public static MedioPago reconstituir(Long id, String nombre, Boolean habilitado) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
