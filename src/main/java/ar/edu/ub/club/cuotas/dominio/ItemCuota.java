package ar.edu.ub.club.cuotas.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import ar.edu.ub.club.socios.dominio.CategoriaSocio;
import ar.edu.ub.club.socios.dominio.Socio;

/**
 * Aporte de cada integrante activo a la cuota, copiado al emitirla: conserva socio, categoría e importe de ese
 * momento. Extiende {@code EntidadPersistente}. Es parte de la composición de {@code Cuota}: se inserta con la
 * cuota ({@code BrokerCuota.guardarPartes}) y no cambia.
 *
 * <p>Casos de uso: CU-05, CU-35, CU-36, CU-37; tareas programadas: emisión mensual de cuotas
 */
public class ItemCuota extends EntidadPersistente {

    /** Integrante. */
    private Socio socio;

    /** Categoría del socio al emitir. */
    private CategoriaSocio categoria;

    /** Importe de la categoría al emitir. */
    private Dinero importe;

    /**
     * Constructor vacío que usa {@code reconstituir}.
     *
     * <p>Casos de uso: todos
     */
    private ItemCuota() {
    }

    /**
     * Agregado en la implementación: crea el aporte de un integrante al emitir la cuota (mensaje «agrega
     * ItemCuota(socio, categoria, importe)» de {@code sec_tmp_emision_p1}).
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @param socio Integrante activo.
     * @param categoria Categoría del integrante al emitir.
     * @param importe Importe de la categoría al emitir.
     */
    public ItemCuota(Socio socio, CategoriaSocio categoria, Dinero importe) {
        this.socio = socio;
        this.categoria = categoria;
        this.importe = importe;
    }

    /**
     * Reconstituye un ítem leído de la base (operación «persistencia» de {@code BrokerItemCuota.crearObjeto});
     * el socio y la categoría llegan después con {@code restaurarAsociaciones}.
     *
     * <p>Casos de uso: CU-05, CU-35, CU-36
     * @param id Identificador.
     * @param importe Importe copiado al emitir.
     * @return el ítem sin asociaciones
     */
    public static ItemCuota reconstituir(Long id, Dinero importe) {
        ItemCuota i = new ItemCuota();
        i.asignarId(id);
        i.importe = importe;
        return i;
    }

    /**
     * Entrega las asociaciones a uno resueltas por {@code BrokerItemCuota.cargarAsociaciones}.
     *
     * <p>Casos de uso: CU-05, CU-35, CU-36
     * @param socio Integrante.
     * @param categoria Categoría al emitir.
     */
    public void restaurarAsociaciones(Socio socio, CategoriaSocio categoria) {
        this.socio = socio;
        this.categoria = categoria;
    }

    /**
     * Devuelve {@code socio}: Integrante.
     *
     * <p>Casos de uso: CU-05, CU-35, CU-36
     * @return el valor del atributo
     */
    public Socio getSocio() {
        return socio;
    }

    /**
     * Devuelve {@code categoria}: Categoría del socio al emitir.
     *
     * <p>Casos de uso: CU-05, CU-35, CU-36
     * @return el valor del atributo
     */
    public CategoriaSocio getCategoria() {
        return categoria;
    }

    /**
     * Devuelve {@code importe}: Importe de la categoría al emitir.
     *
     * <p>Casos de uso: CU-05, CU-35, CU-36
     * @return el valor del atributo
     */
    public Dinero getImporte() {
        return importe;
    }
}
