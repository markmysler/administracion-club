package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;

/**
 * Configuración de la pasarela de pago (fila única, CU-34). Las credenciales no se guardan: solo se registra si
 * están configuradas.
 *
 * <p>Casos de uso: todos
 */
public class ConfiguracionPasarela extends EntidadPersistente {

    /** Identificador de la única fila. */
    private Long ID_UNICO;

    /** Proveedor. */
    private String proveedor;

    /** Adaptador activo. */
    private ModoPasarela modo;

    /** Si se aceptan pagos online. */
    private Boolean habilitada;

    /** Si el entorno tiene credenciales válidas. */
    private Boolean credencialesConfiguradas;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param id valor de {@code id}
     * @param proveedor valor de {@code proveedor}
     * @param modo valor de {@code modo}
     * @param habilitada valor de {@code habilitada}
     * @param credencialesConfiguradas valor de {@code credencialesConfiguradas}
     * @return el resultado
     */
    public static ConfiguracionPasarela reconstituir(Long id, String proveedor, ModoPasarela modo, Boolean habilitada, Boolean credencialesConfiguradas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code ID_UNICO}: Identificador de la única fila.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Long getID_UNICO() {
        return ID_UNICO;
    }

    /**
     * Devuelve {@code proveedor}: Proveedor.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getProveedor() {
        return proveedor;
    }

    /**
     * Devuelve {@code modo}: Adaptador activo.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public ModoPasarela getModo() {
        return modo;
    }

    /**
     * Devuelve {@code habilitada}: Si se aceptan pagos online.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Boolean getHabilitada() {
        return habilitada;
    }

    /**
     * Devuelve {@code credencialesConfiguradas}: Si el entorno tiene credenciales válidas.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Boolean getCredencialesConfiguradas() {
        return credencialesConfiguradas;
    }
}
