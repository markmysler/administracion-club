package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;

/**
 * Medio de pago manual (efectivo, transferencia, etc.) habilitable en CU-34. Baja lógica: no se borra, se
 * deshabilita.
 *
 * <p>Entidad del dominio; la persiste su broker.
 *
 * <p>Casos de uso: CU-34, CU-36, CU-37
 */
public class MedioPago extends EntidadPersistente {

    /** Nombre del medio. */
    private String nombre;

    /** Si puede usarse en pagos nuevos. */
    private Boolean habilitado;

    /**
     * Constructor vacío que usa {@code reconstituir}.
     *
     * <p>Casos de uso: CU-34, CU-36
     */
    private MedioPago() {
    }

    /**
     * Agregado en la implementación: alta de un medio habilitado («create» MedioPago(nombre) de {@code
     * sec_cu34_p3}, FA1).
     *
     * <p>Casos de uso: CU-34
     * @param nombre Nombre del medio (no vacío).
     */
    public MedioPago(String nombre) {
        this.nombre = nombre.trim();
        this.habilitado = true;
    }

    /**
     * Reconstituye un medio leído de {@code medio_pago} (operación «persistencia»).
     *
     * <p>Casos de uso: CU-34, CU-36, CU-37
     * @param id Identificador.
     * @param nombre Nombre.
     * @param habilitado Si está habilitado.
     * @return el medio
     */
    public static MedioPago reconstituir(Long id, String nombre, Boolean habilitado) {
        MedioPago m = new MedioPago();
        m.asignarId(id);
        m.nombre = nombre;
        m.habilitado = habilitado;
        return m;
    }

    /**
     * Agregado en la implementación: habilita o deshabilita el medio (CU-34, paso 5; el diseño dice que la fachada
     * «actualiza habilitado» sin nombrar la operación).
     *
     * <p>Casos de uso: CU-34
     * @param habilitado nuevo valor
     */
    public void cambiarHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    /**
     * Devuelve {@code nombre}: Nombre del medio.
     *
     * <p>Casos de uso: CU-34, CU-36, CU-37
     * @return el valor del atributo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve {@code habilitado}: Si puede usarse en pagos nuevos.
     *
     * <p>Casos de uso: CU-34, CU-36
     * @return el valor del atributo
     */
    public Boolean getHabilitado() {
        return habilitado;
    }
}
