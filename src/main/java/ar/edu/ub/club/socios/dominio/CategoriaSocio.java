package ar.edu.ub.club.socios.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;

/**
 * Categoría de socio con el importe que aporta cada integrante.
 *
 * <p>Entidad del dominio; la persiste su broker.
 *
 * <p>Casos de uso: todos
 */
public class CategoriaSocio extends EntidadPersistente {

    /** Nombre. */
    private String nombre;

    /** Importe mensual. */
    private Dinero importe;

    /** Asignable. */
    private Boolean activa;

    /**
     * Crea una instancia de {@code CategoriaSocio}.
     *
     * <p>Casos de uso: CU-06
     * @param nombre valor de {@code nombre}
     * @param importe valor de {@code importe}
     * @param activa valor de {@code activa}
     */
    public CategoriaSocio(String nombre, Dinero importe, Boolean activa) {
        // PENDIENTE
    }

    /**
     * Aplica los cambios de CU-06.
     *
     * <p>Casos de uso: CU-06
     * @param nombre Nombre.
     * @param importe Importe.
     * @param activa Estado.
     */
    public void modificar(String nombre, Dinero importe, Boolean activa) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param id valor de {@code id}
     * @param nombre valor de {@code nombre}
     * @param importe valor de {@code importe}
     * @param activa valor de {@code activa}
     * @return el resultado
     */
    public static CategoriaSocio reconstituir(Long id, String nombre, Dinero importe, Boolean activa) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code nombre}: Nombre.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve {@code importe}: Importe mensual.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Dinero getImporte() {
        return importe;
    }

    /**
     * Devuelve {@code activa}: Asignable.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Boolean getActiva() {
        return activa;
    }
}
