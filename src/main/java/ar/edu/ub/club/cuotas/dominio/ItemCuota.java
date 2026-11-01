package ar.edu.ub.club.cuotas.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import ar.edu.ub.club.socios.dominio.CategoriaSocio;
import ar.edu.ub.club.socios.dominio.Socio;

/**
 * Aporte de cada integrante activo a la cuota, copiado al emitirla: conserva socio, categoría e importe de ese
 * momento. Extiende {@code EntidadPersistente}.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class ItemCuota extends EntidadPersistente {

    /** Integrante. */
    private Socio socio;

    /** Categoría del socio al emitir. */
    private CategoriaSocio categoria;

    /** Importe de la categoría al emitir. */
    private Dinero importe;

    /**
     * TEMPORAL (API acordada de cuotas).
     *
     * <p>Casos de uso: todos
     * @param socio socio
     * @param categoria categoría
     * @param importe importe
     */
    public ItemCuota(Socio socio, CategoriaSocio categoria, Dinero importe) {
        this.socio = socio;
        this.categoria = categoria;
        this.importe = importe;
    }

    /**
     * TEMPORAL (API acordada de cuotas).
     *
     * <p>Casos de uso: todos
     */
    private ItemCuota() {
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param id valor de {@code id}
     * @param importe valor de {@code importe}
     * @return el resultado
     */
    public static ItemCuota reconstituir(Long id, Dinero importe) {
        ItemCuota i = new ItemCuota();
        i.asignarId(id);
        i.importe = importe;
        return i;
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param socio valor de {@code socio}
     * @param categoria valor de {@code categoria}
     */
    public void restaurarAsociaciones(Socio socio, CategoriaSocio categoria) {
        this.socio = socio;
        this.categoria = categoria;
    }

    /**
     * Devuelve {@code socio}: Integrante.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Socio getSocio() {
        return socio;
    }

    /**
     * Devuelve {@code categoria}: Categoría del socio al emitir.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public CategoriaSocio getCategoria() {
        return categoria;
    }

    /**
     * Devuelve {@code importe}: Importe de la categoría al emitir.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Dinero getImporte() {
        return importe;
    }
}
